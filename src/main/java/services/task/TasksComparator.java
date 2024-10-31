package services.task;

import model.Task;

public class TasksComparator implements java.util.Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        return task1.getName().compareTo(task2.getName());
    }
}
