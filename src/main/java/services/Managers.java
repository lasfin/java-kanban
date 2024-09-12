package services;

import services.history.HistoryService;
import services.history.InMemoryHistoryService;
import services.task.InMemoryTaskService;
import services.task.TaskService;

public class Managers {
    public static TaskService getDefault() {
        return new InMemoryTaskService();
    }
    public static HistoryService getDefaultHistoryService() {
        return new InMemoryHistoryService();
    }
}
