package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    public void add(Task task) {
        int lastTasksCap = 10;
        if (history.size() == lastTasksCap) {
            history.removeFirst();
        }
        history.add(task);
    }

    public List<Task> getHistory() {
        return history;
    }
}
