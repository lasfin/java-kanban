package model;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int parentTaskId;

    public Subtask(String name, String description, int parentTaskId) {
        super(name, description, Status.NEW);
        this.parentTaskId = parentTaskId;
    }

    public Subtask(String name, String description, Status status, int parentTaskId) {
        super(name, description, status);
        this.parentTaskId = parentTaskId;
    }

    public Subtask(String name, String descriptions, Status status, int parentTaskId, long durationInMinutes, LocalDateTime startTime) {
        super(name, descriptions, status, durationInMinutes, startTime);
        this.parentTaskId = parentTaskId;
    }

    public int getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Epic parentTask) {
        this.parentTaskId = parentTask.getId();
    }

    public void setParentTaskId(int parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public String toString() {
        return super.toString() + " - Parent task: " + parentTaskId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
