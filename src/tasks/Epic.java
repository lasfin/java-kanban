package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, Status status, ArrayList<Subtask> subtasks) {
        super(name, description, status);
        this.subtasks = subtasks;
        for (Subtask subtask : subtasks) {
            subtask.setParentTask(this);
        }
    }

    public ArrayList<Subtask> getTasks() {
        return subtasks;
    }

    @Override
    public Status getStatus() {
        int newCounter = 0;
        if (subtasks.isEmpty()) {
            return Status.NEW;
        }
        for (Task task : subtasks) {
            if (task.getStatus() != Status.DONE) {
                return Status.IN_PROGRESS;
            }
            if (task.getStatus() == Status.NEW) {
                newCounter++;
            }
        }

        if (newCounter == subtasks.size()) {
            return Status.NEW;
        }

        return Status.DONE;
    }
}
