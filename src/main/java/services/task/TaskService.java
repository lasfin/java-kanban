package services.task;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskService {
    public ArrayList<Task> getTasks();
    public ArrayList<Epic> getEpics();
    public ArrayList<Subtask> getSubtasks();

    public void addTask(Task task);
    public void removeTask(Task task);

    public void addEpic(Epic epic);
    public void removeEpic(Epic epic);

    public void addSubtask(Subtask subtask);
    public void removeSubtask(Subtask subtask);

    public Task getTask(int id);
    public Epic getEpic(int id);
    public Subtask getSubtask(int id);

    public void updateSubtask(Subtask subtask);
    public void updateTask(Task task);
    public void updateEpic(Epic epic);

    public void removeAll();

    /**
     * Метод должен возвращать последние 10 просмотренных задач.
     * Просмотром будем считать вызов тех методов, которые получают задачу по идентификатору, —
     * getTask(int id), getSubtask(int id) и getEpic(int id). От повторных просмотров избавляться не нужно.
     */
    public List<Task> getHistory();
}
