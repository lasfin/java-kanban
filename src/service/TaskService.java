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

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
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

        for (Subtask subtask : epic.getSubtasks()) {
            lastId++;
            subtask.setId(lastId);

            subtasks.put(lastId, subtask);
        }
    }

    public void addSubtask(Subtask subtask) {
        int newId = lastId + 1;
        subtask.setId(newId);

        subtasks.put(newId, subtask);
        Epic epic = subtask.getParentTask();
        epic.addSubtask(subtask);
    }

    public void removeTask(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            removeEpic(id);
        } else if (subtasks.containsKey(id)) {
            removeSubtask(id);
        }
    }

    private void removeSubtask(Integer id) {
        Epic epic = subtasks.get(id).getParentTask();
        if (epic != null) {
            Subtask subtask = subtasks.get(id);
            epic.removeSubtask(subtask);
        }
        subtasks.remove(id);
    }

    private void removeEpic(Integer id) {
        ArrayList<Subtask> epicSubtasks = epics.get(id).getSubtasks();
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }


    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
        }
        Epic epic = subtask.getParentTask();
        epic.setStatus(epic.calculateStatus());
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
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
