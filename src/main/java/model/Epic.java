package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, Status.NEW);
    }

    public void replaceSubtask(Subtask subtask) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == subtask.getId()) {
                subtasks.set(i, subtask);
                this.setStatus(calculateStatus());
                return;
            }
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        this.setStatus(calculateStatus());
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        this.setStatus(calculateStatus());
    }

    public Status calculateStatus() {
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

    private String showAllSubtasksIds() {
        StringBuilder result = new StringBuilder();
        for (Subtask subtask : subtasks) {
            result.append(subtask.getId()).append(", ");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " - Subtasks: " + showAllSubtasksIds();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }
}
