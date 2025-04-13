package db;

import db.exception.InvalidEntityException;

public interface Serializer {
    String serialize(Entity e);
    Entity deserialize(String s) throws InvalidEntityException;
}
