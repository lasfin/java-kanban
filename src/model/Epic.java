package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    private String showAllSubtasksIds() {
        StringBuilder result = new StringBuilder();
        for (Subtask subtask : subtasks) {
            result.append(subtask.getId()).append(", ");
        }
        return result.toString();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        subtask.setParentTask(this);
    }

    @Override
    public Status getStatus() {
        int newCounter = 0;
        int doneCounter = 0;
        if (subtasks.isEmpty()) {
            return Status.NEW;
        }
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                return Status.IN_PROGRESS;
            }
            if (subtask.getStatus() == Status.DONE) {
                doneCounter++;
            }
            if (subtask.getStatus() == Status.NEW) {
                newCounter++;
            }
        }

        if (newCounter == subtasks.size()) {
            return Status.NEW;
        }

        if (doneCounter < subtasks.size()) {
            return Status.IN_PROGRESS;
        }

        return Status.DONE;
    }

    @Override
    public String toString() {
        return super.toString() + " - Subtasks: " + showAllSubtasksIds();
    }
}
