package services.exeptions;

public class TasksServiceSaveException extends RuntimeException {
    public TasksServiceSaveException(String message) {
        super(message);
    }
}
