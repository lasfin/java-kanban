package manager;

import tasks.Task;

import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskHashMap;
    private Integer lastId;

    public TaskManager() {
        taskHashMap = new HashMap<>();
    }

    public HashMap<Integer, Task> getTasks() {
        return taskHashMap;
    }




}
