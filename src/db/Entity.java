package db;

public abstract class Entity {
    public int id;

    public Entity() {
        this.id = -1; //the default value for id
    }
    public Entity ( int id){
        this.id= id;
    }

    public abstract Entity copy();

    public abstract int getEntityCode();

}
