package main.service;

import main.model.Epic;
import main.model.Subtask;
import main.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskService implements TaskService {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryHistoryManager();

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
    }

    @Override
    public void addEpic(Epic epic) {
        lastId++;
        epic.setId(lastId);
        epics.put(lastId, epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        lastId++;
        subtask.setId(lastId);

        subtasks.put(lastId, subtask);
        Epic epic = subtask.getParentTask();
        epic.addSubtask(subtask);
    }

    @Override
    public void removeTask(Task task) {
        tasks.remove(task.getId());
    }

    @Override
    public void removeSubtask(Subtask subtask) {
        Epic epic = subtask.getParentTask();
        epic.removeSubtask(subtask);
        subtasks.remove(subtask.getId());
    }

    @Override
    public void removeEpic(Epic epic) {
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = subtask.getParentTask();
        epic.replaceSubtask(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    private void markTaskAsLast(Task task) {
        historyManager.add(task);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
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

    public ArrayList<Subtask> getEpicTasks(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            return epic.getSubtasks();
        }

        return new ArrayList<>();
    }

    @Override
    public void removeAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
