package db;
import db.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private static ArrayList<Entity> entities = new ArrayList<>();
    private static HashMap<Integer, Validator> validators;


    private static int currentId = 1;

    public static void add(Entity e) {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        e.id=currentId++;
        entities.add(e.copy());

    }
    public static Entity get(int id) throws EntityNotFoundException{
        for (Entity entity : entities)
            if (entity.id == id)
                return entity.copy();

        throw new EntityNotFoundException (id);
    }

    public static void delete(int id) throws EntityNotFoundException{
        if (entities.remove(id-1) == null)
            throw new EntityNotFoundException();

    }

    public static void update(Entity e) throws EntityNotFoundException{

        boolean entityFound = false;
        for (int i=0; i<entities.size(); i++) {

            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                entityFound = true;
                break;
            }
        }
        if (!entityFound)
            throw new EntityNotFoundException("Entity with ID " + e.id + " not found.");

    }

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode))
            throw new IllegalArgumentException ("Validator is already registered.");
        validators.put(entityCode, validator);
    }
}




