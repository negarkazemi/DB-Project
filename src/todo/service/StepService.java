package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;
import todo.validator.StepValidator;

import java.util.Date;
import java.util.Scanner;

import static todo.entity.Step.STEP_ENTITY_CODE;

public class StepService {
    public static void saveStep(int taskRef, String title) throws InvalidEntityException {
        Step step = new Step(title, taskRef);
        step.status = Step.Status.NotStarted;
        Database.add(step);
    }

    public static void addStep() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("TaskID: ");
            int taskRef = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Title: ");
            String title = scanner.nextLine();

            if (!(Database.getValidators().containsKey(STEP_ENTITY_CODE))) {
                Database.registerValidator(STEP_ENTITY_CODE, new StepValidator());
            }

            Step step = new Step(title, taskRef);
            step.status = Step.Status.NotStarted;
            Database.add(step);


            System.out.println("Step saved successfully.");
            System.out.println("ID: " + step.id);
            System.out.println("Creation Date: " + new Date());
        } catch (InvalidEntityException | IllegalArgumentException e) {
            System.out.println("Cannot save step.");
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Cannot save step.");
            System.out.println("Error: Unexpected error occurred.");
        }
    }

    public static void updateStep() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ID: ");
        int stepId = scanner.nextInt();
        scanner.nextLine();

        try {
            Entity entity = Database.findById(stepId);

            if (!(entity instanceof Step step)) {
                throw new InvalidEntityException("Entity is not a Step.");
            }

            System.out.print("Field: ");
            String field = scanner.nextLine();
            System.out.print("New Value: ");
            String newValue = scanner.nextLine();

            String oldValue;

            switch (field) {
                case "status":
                    oldValue = step.status.name();
                    step.status = Step.Status.valueOf(newValue);
                    break;
                case "title":
                    oldValue = step.title;
                    step.title = newValue;
                    break;
                default:
                    System.out.println("Unknown field: " + field);
                    return;
            }

            Database.update(step);


            Task task = (Task) Database.findById(step.taskRef);

            if (task.status != Task.Status.Completed) {
                boolean stepsCompleted = true;
                for (Entity e : Database.getAll(Step.STEP_ENTITY_CODE)) {
                    if (e instanceof Step s && s.taskRef == task.id && s.status != Step.Status.Completed) {
                        stepsCompleted = false;
                        break;
                    }
                }
                if (stepsCompleted) {
                    TaskService.setAsCompleted(step.taskRef);
                } else if (step.status == Step.Status.Completed && task.status == Task.Status.NotStarted) {
                    task.status = Task.Status.InProgress;
                }

                Database.update(task);
            }
            System.out.println("Successfully updated the step.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + newValue);
            System.out.println("Modification Date: " + new Date());
        } catch (EntityNotFoundException | InvalidEntityException e) {
            System.out.println("Cannot update step with ID=" + stepId);
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Something happened, " + e.getMessage());
        }
    }
}
