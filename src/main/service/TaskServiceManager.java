package main.service;

public class TaskServiceManager {
    public static TaskService getDefault() {
        return new InMemoryTaskService();
    }
}
