package todo.service;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;

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
}
