package services.task;

public class TaskServiceManager {
    public static TaskService getDefault() {
        return new InMemoryTaskService();
    }
}
