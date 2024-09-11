package services.history;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private final int lastTasksCap;

    public InMemoryHistoryHistoryManager(int lastTasksCap) {
        this.lastTasksCap = lastTasksCap;
    }

    public void add(Task task) {
        if (history.size() == lastTasksCap) {
            history.remove(0);
        }
        history.add(task);
    }

    public List<Task> getHistory() {
        return history;
    }
}
