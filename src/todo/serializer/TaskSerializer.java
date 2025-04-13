package todo.serializer;

import db.Entity;
import db.Serializer;
import db.exception.InvalidEntityException;
import todo.entity.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TaskSerializer implements Serializer {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String serialize(Entity e) {
        if (!(e instanceof Task task)) {
            throw new IllegalArgumentException("Expected a Task entity.");
        }

        // id|title|description|dueDate|status
        return task.id + "|" +
                task.title + "|" +
                task.description + "|" +
                formatter.format(task.dueDate) + "|" +
                task.status.name();
    }


    @Override
    public Entity deserialize(String s) throws InvalidEntityException {
        try {
            String[] parts = s.split("\\|", -1);

            int id = Integer.parseInt(parts[0]);
            String title = parts[1];
            String description = parts[2];
            var dueDate = formatter.parse(parts[3]);
            var status = Task.Status.valueOf(parts[4]);

            Task task = new Task(title, description, dueDate);
            task.id = id;
            task.status = status;

            return task;
        } catch (ParseException | IllegalArgumentException e) {
            throw new InvalidEntityException("Failed to deserialize Task: " + e.getMessage());
        }
    }
}
