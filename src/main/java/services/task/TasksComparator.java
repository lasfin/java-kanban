package services.task;

import model.Task;

public class TasksComparator implements java.util.Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getStartTime() != null && task2.getStartTime() != null) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        }

        return task1.getName().compareTo(task2.getName());
    }
}
