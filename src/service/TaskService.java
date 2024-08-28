package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskService {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    private Integer lastId = 0;

    public TaskService() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

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


    public void updateSubtask(Integer id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            subtasks.put(id, subtask);
        }
    }

    public void updateEpic(Integer id, Epic epic) {
        if (epics.containsKey(id)) {
            epics.put(id, epic);
        }
    }

    public void updateTask(Integer id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
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
