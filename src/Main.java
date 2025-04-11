
import todo.service.StepService;
import todo.service.TaskService;

import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                break;
            }

            if (command.equalsIgnoreCase("add task")) {
                System.out.print("Title: ");
                String title = scanner.nextLine();
                System.out.print("Description: ");
                String description = scanner.nextLine();
                System.out.print("Due date (yyyy-MM-dd): ");
                String dateInput = scanner.nextLine();

                TaskService.addTask(title, description, dateInput);
            }

            if (command.equalsIgnoreCase("add step")) {
                StepService.addStep();
            }

            if (command.equalsIgnoreCase("delete")) {
                System.out.print("Task ID: ");
                int taskId = scanner.nextInt();
                scanner.nextLine();

                TaskService.deleteTask(taskId);
            }

            if (command.equalsIgnoreCase("update task")) {
                System.out.print("Task ID: ");
                int taskId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Field: ");
                String field = scanner.nextLine();
                System.out.print("New Value: ");
                String newValue = scanner.nextLine();

                TaskService.updateTask(taskId, field, newValue);
            }
            if (command.equalsIgnoreCase("update step")) {
                StepService.updateStep();
            }

            if (command.equalsIgnoreCase("get task-by-id")) {
                System.out.print("Task ID: ");
                int taskId = scanner.nextInt();
                scanner.nextLine();

                TaskService.getTaskById(taskId);
            }

            if (command.equalsIgnoreCase("get all-tasks")) {
                TaskService.getAllTasks();
            }

            if (command.equalsIgnoreCase("get incomplete-tasks")) {
                TaskService.getIncompleteTasks();

            }
        }
    }
}




