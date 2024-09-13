package services.task;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskService {
     ArrayList<Task> getTasks();

     ArrayList<Epic> getEpics();

     ArrayList<Subtask> getSubtasks();

     void addTask(Task task);

     void removeTask(Task task);

     void addEpic(Epic epic);

     void removeEpic(Epic epic);

     void addSubtask(Subtask subtask);

     void removeSubtask(Subtask subtask);

     Task getTask(int id);

     Epic getEpic(int id);

     Subtask getSubtask(int id);

     void updateSubtask(Subtask subtask);

     void updateTask(Task task);

     void updateEpic(Epic epic);

     void removeAll();

     List<Task> getHistory();
}
