package services.exeptions;

public class TaskServiceValidationException extends RuntimeException {
    public TaskServiceValidationException(String message) {
        super(message);
    }
}
