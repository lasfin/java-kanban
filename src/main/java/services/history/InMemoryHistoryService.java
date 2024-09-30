package services.history;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryService implements HistoryService {
    private final List<Task> history = new ArrayList<>();
    private final static int LAST_TASKS_CAP = 10;

    @Override
    public void add(Task task) {
        if (history.size() == LAST_TASKS_CAP) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public void remove(int id) {
        history.removeIf(task -> task.getId() == id);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
