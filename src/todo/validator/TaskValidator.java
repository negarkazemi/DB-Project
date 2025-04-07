package todo.validator;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import todo.entity.Task;

public class TaskValidator implements Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException{

        if (!(entity instanceof Task)) {
            throw new IllegalArgumentException("Expected a Task entity.");
        }

        Task task = (Task) entity;

        if (task.title == null || task.title.trim().isEmpty()) {
            throw new InvalidEntityException("Task title cannot be null or empty.");
        }

    }
}