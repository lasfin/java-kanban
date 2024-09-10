package model;

public class Subtask extends Task {
    private Epic parentTask;

    public Subtask(String name, String description, Status status, Epic parentTask) {
        super(name, description, status);
        this.parentTask = parentTask;
    }

    public Epic getParentTask() {
        return parentTask;
    }

    public void setParentTask(Epic parentTask) {
        this.parentTask = parentTask;
    }

    @Override
    public String toString() {
        return super.toString() + " - Parent task: " + parentTask.getId() + ": " + parentTask.getName();
    }
}
