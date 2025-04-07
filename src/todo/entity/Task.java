package todo.entity;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Task extends Entity implements Trackable {

    public enum Status {
        NotStarted,
        InProgress,
        Completed
    }

    public String title;
    public String description;
    public Date dueDate;
    public Status status;

    private Date creationDate;
    private Date lastModificationDate;

    public static final int TASK_ENTITY_CODE = 10;

    public Task(String title, String description, Date dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.NotStarted; // default
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    @Override
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    @Override
    public int getEntityCode() {
        return TASK_ENTITY_CODE;
    }

    @Override
    public Task copy() {
        Task copyTask = new Task(this.title, this.description, new Date(this.dueDate.getTime()));
        copyTask.id = this.id;
        copyTask.status = this.status;
        copyTask.creationDate =new Date(this.creationDate.getTime());
        copyTask.lastModificationDate =new Date(this.lastModificationDate.getTime());
        return copyTask;
    }


}
