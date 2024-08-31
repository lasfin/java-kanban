package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskService {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private Integer lastId = 0;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void addTask(Task task) {
        lastId++;
        task.setId(lastId);

        tasks.put(lastId, task);
    }

    public void addEpic(Epic epic) {
        lastId++;
        epic.setId(lastId);
        epics.put(lastId, epic);
    }

    public void addSubtask(Subtask subtask) {
        lastId++;
        subtask.setId(lastId);

        subtasks.put(lastId, subtask);
        Epic epic = subtask.getParentTask();
        epic.addSubtask(subtask);
    }

    public void removeTask(Task task) {
        tasks.remove(task.getId());
    }

    public void removeSubtask(Subtask subtask) {
        Epic epic = subtask.getParentTask();
        epic.removeSubtask(subtask);
        subtasks.remove(subtask.getId());
    }

    public void removeEpic(Epic epic) {
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(epic.getId());
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = subtask.getParentTask();
        epic.replaceSubtask(subtask);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task getTask(Integer id) {
        return tasks.get(id);
    }

    public Epic getEpic(Integer id) {
        return epics.get(id);
    }

    public Subtask getSubtask(Integer id) {
        return subtasks.get(id);
    }

    public ArrayList<Subtask> getEpicTasks(Integer id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            return epic.getSubtasks();
        }

        return new ArrayList<>();
    }

    public void removeAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }
}
