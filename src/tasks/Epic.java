package tasks;

public class Epic extends Task {
    private Task[] tasks;

    public Epic(String name, String description, Status status, Task[] tasks) {
        super(name, description, status);
        this.tasks = tasks;
    }

    public Task[] getTasks() {
        return tasks;
    }

    @Override
    public Status getStatus() {
        int newCounter = 0;
        if (tasks.length == 0) {
            return Status.NEW;
        }
        for (Task task : tasks) {
            if (task.getStatus() != Status.DONE) {
                return Status.IN_PROGRESS;
            }
            if (task.getStatus() == Status.NEW) {
                newCounter++;
            }
        }

        if (newCounter == tasks.length) {
            return Status.NEW;
        }

        return Status.DONE;
    }
}
