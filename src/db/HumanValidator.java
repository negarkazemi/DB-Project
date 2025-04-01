package db;

import db.exception.InvalidEntityException;
import example.Human;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {

        if (!(entity instanceof Human)) {
            throw new IllegalArgumentException("Invalid entity type: Expected a Human.");
        }

        Human human = (Human) entity;

        if (human.name == null || human.name.isEmpty()) {
            throw new InvalidEntityException("Human name cannot be empty.");
        }
        if (human.age < 0) {
            throw new InvalidEntityException("Human age cannot be negative.");
        }
    }
}