package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;
import todo.validator.TaskValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static todo.entity.Task.TASK_ENTITY_CODE;

public class TaskService {

    public static void setAsCompleted(int taskId) throws InvalidEntityException {
        try {
            Task task = (Task) Database.findById(taskId);

            task.status = Task.Status.Completed;

            Database.update(task);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Task with ID " + taskId + " not found.");
        }
    }

    public static void addTask(String title, String description, String dateInput) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dueDate = formatter.parse(dateInput);

            if (!(Database.getValidators().containsKey(TASK_ENTITY_CODE))) {
                Database.registerValidator(TASK_ENTITY_CODE, new TaskValidator());
            }
            Task task = new Task(title, description, dueDate);
            Database.add(task);

            System.out.println("Task saved successfully.");
            System.out.println("ID: " + task.id);
        } catch (InvalidEntityException | IllegalArgumentException e) {
            System.out.println("Cannot save task.");
            System.out.println("Error: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Cannot save task.");
            System.out.println("Error: Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    public static void deleteTask(int entityId) {
        try {
            Entity entityToDelete = Database.findById(entityId);

            if (entityToDelete instanceof Task) {
                List<Integer> stepIdsToDelete = new ArrayList<>();
                for (Entity entity : Database.getAll(Step.STEP_ENTITY_CODE)) {
                    if (entity instanceof Step) {
                        Step step = (Step) entity;
                        if (step.taskRef == entityId) {
                            stepIdsToDelete.add(step.id);
                        }
                    }
                }

                for (int stepId : stepIdsToDelete) {
                    Database.delete(stepId);
                }
            }

            Database.delete(entityId);
            System.out.println("Entity with ID=" + entityId + " successfully deleted.");
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot delete entity with ID= " + entityId);
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Cannot delete entity with ID= " + entityId);
            System.out.println("Error:  Something happened, " + e.getMessage());
        }
    }

    public static void updateTask(int taskId, String field, String newValue) {
        try {
            Entity entity = Database.findById(taskId);

            if (!(entity instanceof Task task)) {
                throw new InvalidEntityException("Entity is not a Task.");
            }

            String oldValue;
            switch (field) {
                case "title":
                    oldValue = task.title;
                    task.title = newValue;
                    break;
                case "description":
                    oldValue = task.description;
                    task.description = newValue;
                    break;
                case "status":
                    oldValue = task.status.name();
                    task.status = Task.Status.valueOf(newValue);
                    if (task.status == Task.Status.Completed) {
                        for (Entity e : Database.getAll(Step.STEP_ENTITY_CODE)) {
                            if (e instanceof Step step && step.taskRef == taskId) {
                                step.status = Step.Status.Completed;
                                Database.update(step);
                            }
                        }
                    }
                    break;
                case "dueDate":
                    oldValue = task.dueDate.toString();
                    task.dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(newValue);
                    break;
                default:
                    System.out.println("Unknown field: " + field);
                    return;
            }

            Database.update(task);

            System.out.println("Successfully updated the task.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + newValue);
            System.out.println("Modification Date: " + new Date());

        } catch (EntityNotFoundException | InvalidEntityException e) {
            System.out.println("Cannot update task with ID=" + taskId);
            System.out.println("Error: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Cannot update task with ID=" + taskId);
            System.out.println("Error: Invalid date format. Please use yyyy-MM-dd.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getTaskById(int taskId){

        try {
            Task task = (Task) Database.findById(taskId);

            System.out.println("ID: " + task.id);
            System.out.println("Title: " + task.title);
            System.out.println("Due Date: " + task.dueDate);
            System.out.println("Status: " + task.status);

            List<Step> steps = new ArrayList<>();
            for (Entity entity : Database.getAll(Step.STEP_ENTITY_CODE)) {
                if (entity instanceof Step step && step.taskRef == taskId) {
                    steps.add(step);
                }
            }

            if (!steps.isEmpty()) {
                System.out.println("Steps:");
                for (Step step : steps) {
                    System.out.println("    + " + step.title + ":");
                    System.out.println("        ID: " + step.id);
                    System.out.println("        Status: " + step.status);
                }
            } else {
                System.out.println("This task has no steps.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot find task with ID= " + taskId + ".");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getAllTasks(){
        try {
            ArrayList<Task> tasks = new ArrayList<>();

            for (Entity entity : Database.getAll(Task.TASK_ENTITY_CODE)) {
                if (entity instanceof Task task) {
                    tasks.add(task);
                }
            }

            tasks.sort(Comparator.comparing(task -> task.dueDate));

            for (Task task : tasks) {
                System.out.println("ID: " + task.id);
                System.out.println("Title: " + task.title);
                System.out.println("Due Date: " + new SimpleDateFormat("yyyy-MM-dd").format(task.dueDate));
                System.out.println("Status: " + task.status);

                ArrayList<Step> taskSteps = new ArrayList<>();
                for (Entity e : Database.getAll(Step.STEP_ENTITY_CODE)) {
                    if (e instanceof Step step && step.taskRef == task.id) {
                        taskSteps.add(step);
                    }
                }

                if (!(taskSteps.isEmpty())) {
                    System.out.println("Steps:");
                    for (Step step : taskSteps) {
                        System.out.println("    + " + step.title + ":");
                        System.out.println("        ID: " + step.id);
                        System.out.println("        Status: " + step.status);
                    }
                }
                System.out.println("\n");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getIncompleteTasks(){
        try {
            ArrayList<Task> tasks = new ArrayList<>();

            for (Entity entity : Database.getAll(Task.TASK_ENTITY_CODE)) {
                if (entity instanceof Task task) {
                    tasks.add(task);
                }
            }

            for (Task task : tasks) {
                if (task.status != Task.Status.Completed) {
                    System.out.println("ID: " + task.id);
                    System.out.println("Title: " + task.title);
                    System.out.println("Due Date: " + new SimpleDateFormat("yyyy-MM-dd").format(task.dueDate));
                    System.out.println("Status: " + task.status);


                    ArrayList<Step> taskSteps = new ArrayList<>();
                    for (Entity e : Database.getAll(Step.STEP_ENTITY_CODE)) {
                        if (e instanceof Step step && step.taskRef == task.id) {
                            taskSteps.add(step);
                        }
                    }

                    if (!taskSteps.isEmpty()) {
                        System.out.println("Steps:");
                        for (Step step : taskSteps) {
                            System.out.println("    + " + step.title + ":");
                            System.out.println("        ID: " + step.id);
                            System.out.println("        Status: " + step.status);
                        }
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
