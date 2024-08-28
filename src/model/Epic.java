package model;

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

    @Override public String toString() {
        return super.toString() + " - Subtasks: " + showAllSubtasksIds();
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

    @Override
    public Status getStatus() {
        int newCounter = 0;
        if (subtasks.isEmpty()) {
            return Status.NEW;
        }
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                return Status.IN_PROGRESS;
            }
            if (subtask.getStatus() == Status.NEW) {
                newCounter++;
            }
        }

        if (newCounter == subtasks.size()) {
            return Status.NEW;
        }

        return Status.DONE;
    }
}
