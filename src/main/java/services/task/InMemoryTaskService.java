package services.task;

import model.Epic;
import model.Subtask;
import model.Task;
import services.Managers;
import services.exeptions.TaskServiceNotFoundException;
import services.history.HistoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskService implements TaskService {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final TreeSet<Task> sortedTasks = new TreeSet<>(new TasksComparator());
    private final HistoryService historyService = Managers.getDefaultHistoryService();

    private Integer lastId = 0;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void addTask(Task task) {
        lastId++;
        task.setId(lastId);

        tasks.put(lastId, task);
        if (task.getStartTime() != null) {
            sortedTasks.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        lastId++;
        epic.setId(lastId);

        epics.put(lastId, epic);
        if (epic.getStartTime() != null) {
            sortedTasks.add(epic);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        lastId++;
        subtask.setId(lastId);
        subtasks.put(lastId, subtask);

        int parentTaskId = subtask.getParentTaskId();

        Epic epic = epics.get(parentTaskId);
        epic.addSubtask(subtask);

        if (subtask.getStartTime() != null) {
            sortedTasks.add(subtask);
        }
    }

    @Override
    public void removeTask(Task task) {
        tasks.remove(task.getId());
        historyService.remove(task.getId());
        sortedTasks.remove(task);
    }

    @Override
    public void removeSubtask(Subtask subtask) {
        int parentTaskId = subtask.getParentTaskId();
        Epic epic = epics.get(parentTaskId);
        epic.removeSubtask(subtask);

        subtasks.remove(subtask.getId());
        historyService.remove(subtask.getId());
        sortedTasks.remove(subtask);
    }

    @Override
    public void removeEpic(Epic epic) {
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
            historyService.remove(subtask.getId());
            sortedTasks.remove(subtask);
        }

        epics.remove(epic.getId());
        historyService.remove(epic.getId());
        sortedTasks.remove(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int parentTaskId = subtask.getParentTaskId();
        Subtask oldSubtask = subtasks.get(subtask.getId());

        Epic epic = epics.get(parentTaskId);
        subtasks.put(subtask.getId(), subtask);
        epic.replaceSubtask(subtask);

        if (sortedTasks.contains(oldSubtask)) {
            sortedTasks.remove(oldSubtask);
            sortedTasks.add(subtask);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        epics.put(epic.getId(), epic);

        if (sortedTasks.contains(oldEpic)) {
            sortedTasks.remove(oldEpic);
            sortedTasks.add(epic);
        }
    }

    @Override
    public void updateTask(Task task) {
        Task oldTask = tasks.get(task.getId());
        tasks.put(task.getId(), task);

        if (sortedTasks.contains(oldTask)) {
            sortedTasks.remove(oldTask);
            sortedTasks.add(task);
        }
    }

    private void markTaskAsLast(Task task) {
        if (task != null) {
            historyService.add(task);
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);

        if (task == null) {
            throw new TaskServiceNotFoundException("Task with id " + id + " not found");
        }

        markTaskAsLast(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        markTaskAsLast(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        markTaskAsLast(subtask);
        return subtask;
    }

    @Override
    public void removeAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyService.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }
}
