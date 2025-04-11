package todo.entity;

import db.Entity;

public class Step extends Entity {

    public enum Status {
        NotStarted,
        Completed
    }

    public String title;
    public Status status;
    public int taskRef;

    public static final int STEP_ENTITY_CODE = 16;

    public Step(String title, int taskRef) {
        this.title = title;
        this.taskRef = taskRef;
        this.status = Status.NotStarted; // default
    }

    @Override
    public int getEntityCode() {
        return STEP_ENTITY_CODE;
    }

    @Override
    public Step copy() {
        Step copyStep = new Step(this.title, this.taskRef);
        copyStep.id = this.id;
        copyStep.status = this.status;
        return copyStep;
    }
}
