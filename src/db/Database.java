package db;
import db.exception.EntityNotFoundException;

import java.util.ArrayList;

public class Database {

    private static ArrayList<Entity> entities = new ArrayList<>();

//    private Database(){
//        entities = new ArrayList<>();
//    }

    private static int currentId = 1;

    public static void add(Entity e) {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        entities.add(e);
        e.id=currentId++;
    }
    public static Entity get(int id) throws EntityNotFoundException{
        for (Entity entity : entities)
            if (entity.id == id)
                return entity;

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
                entities.set(i, e);
                entityFound = true;
                break;
            }
        }
        if (!entityFound)
            throw new EntityNotFoundException("Entity with ID " + e.id + " not found.");

    }
}




