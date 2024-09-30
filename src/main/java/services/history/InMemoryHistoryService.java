package services.history;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryService implements HistoryService {
    private final HashMap<Integer, LinkedList<Task>> history = new HashMap<>();
    private final LinkedList<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyList.isEmpty()) {
            historyList.add(task);
        } else {
            historyList.addLast(task);
        }

        history.put(
                task.getId(),
                history.getOrDefault(task.getId(),historyList)
        );
    }

    @Override
    public void remove(int id) {
        historyList.removeIf(task -> task.getId() == id);
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}
