package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Database {

    private static ArrayList<Entity> entities = new ArrayList<>();
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    private static int currentId = 1;

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode))
            throw new IllegalArgumentException("Validator is already registered.");
        validators.put(entityCode, validator);
    }

    public static void add(Entity e) throws InvalidEntityException {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Validator validator = validators.get(e.getEntityCode());
        validator.validate(e);

        if (e instanceof Trackable){
            Trackable trackableEntity = (Trackable) e;
            Date now = new Date();
            trackableEntity.setCreationDate(now);
            trackableEntity.setLastModificationDate(now);
        }

        e.id = currentId++;
        entities.add(e.copy());

    }


    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity entity : entities)
            if (entity.id == id)
                return entity.copy();

        throw new EntityNotFoundException(id);
    }


    public static void delete(int id) throws EntityNotFoundException {
        if (entities.remove(id - 1) == null)
            throw new EntityNotFoundException();

        entities.remove(id - 1);
    }


    public static void update(Entity e) throws EntityNotFoundException, InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        validator.validate(e);

        boolean entityFound = false;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                if (e instanceof Trackable){
                    Trackable trackableEntity = (Trackable) e;
                    Date now = new Date();
                    trackableEntity.setLastModificationDate(now);
                }
                entities.set(i, e.copy());
                entityFound = true;
                break;
            }
        }

        if (!entityFound)
            throw new EntityNotFoundException("Entity with ID " + e.id + " not found.");
    }
}




