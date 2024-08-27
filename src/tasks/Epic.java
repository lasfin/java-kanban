package tasks;

public class Epic extends Task {
    private Subtask[] subtasks;

    public Epic(String name, String description, Status status, Subtask[] subtasks) {
        super(name, description, status);
        this.subtasks = subtasks;
    }

    public Subtask[] getTasks() {
        return subtasks;
    }

    @Override
    public Status getStatus() {
        int newCounter = 0;
        if (subtasks.length == 0) {
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

        if (newCounter == subtasks.length) {
            return Status.NEW;
        }

        return Status.DONE;
    }
}
