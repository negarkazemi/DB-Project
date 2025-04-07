package todo.validator;

import db.Database;
import db.Entity;
import db.Validator;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {

        if (!(entity instanceof Task)) {
            throw new IllegalArgumentException("Expected a Task entity.");
        }

        Step step = (Step) entity;

        if (step.title == null || step.title.trim().isEmpty()) {
            throw new InvalidEntityException("Step title cannot be null or empty.");
        }

        try {
            Entity task = Database.findById(step.taskRef);
            if (!(task instanceof Task)) {
                throw new InvalidEntityException("Referenced task ID does not point to a valid Task.");
            }
        } catch (Exception e) {
            throw new EntityNotFoundException("Task with ID " + step.taskRef + " not found.");
        }

    }
}
