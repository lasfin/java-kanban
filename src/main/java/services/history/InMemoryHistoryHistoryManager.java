package services.history;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    public void add(Task task) {
        int lastTasksCap = 10;
        if (history.size() == lastTasksCap) {
            history.remove(0);
        }
        history.add(task);
    }

    public List<Task> getHistory() {
        return history;
    }
}
