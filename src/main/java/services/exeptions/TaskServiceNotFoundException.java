package services.exeptions;

public class TaskServiceNotFoundException extends RuntimeException {
    public TaskServiceNotFoundException(String message) {
        super(message);
    }
}
