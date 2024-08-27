package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    private Integer lastId = 0;

    public TaskManager() {
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
        tasks.put(++lastId, task);
    }

    public void addEpic(Epic epic) {
        epics.put(++lastId, epic);
        for (Task task : epic.getTasks()) {
            tasks.put(++lastId, task);
        }
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(++lastId, subtask);
    }

    public void removeTask(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        }
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

    public void deleteTask(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        }
    }

    public Task[] getEpicTasks(Integer id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            return epic.getTasks();
        }

        return new Task[0];
    }

    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }






}
