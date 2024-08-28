package model;

public class Subtask extends Task {
    private Epic parentTask;

    public Subtask(String name, String description, Status status, Epic parentTask) {
        super(name, description, status);
        this.parentTask = parentTask;
    }

    @Override public String toString() {
        if (parentTask == null) {
            return super.toString() + " - Parent task: null";
        }

        return super.toString() + " - Parent task: " + parentTask.getId() + ": " + parentTask.getName();
    }

    public Epic getParentTask() {
        return parentTask;
    }

    public void setParentTask(Epic parentTask) {
        this.parentTask = parentTask;
    }
}
