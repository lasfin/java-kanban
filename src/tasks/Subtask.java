package tasks;

public class Subtask extends Task {
    private Task parentTask;

    public Subtask(String name, String description, Status status, Task parentTask) {
        super(name, description, status);
        this.parentTask = parentTask;
    }

    @Override public String toString() {
        if (parentTask == null) {
            return super.toString() + " - Parent task: null";
        }

        return super.toString() + " - Parent task: " + parentTask.getName();
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }
}
