import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Create two new tasks
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a second task for the project", Status.NEW);

        // Create a new epic (with 2 subtask)
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask("Subtask 1", "Subtask 1 description", Status.NEW, null));
        subtasks.add(new Subtask("Subtask 2", "Subtask 2 description", Status.IN_PROGRESS, null));
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW, subtasks);

        // create a new epic (with 1 subtask)
        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(new Subtask("Subtask 3", "Subtask 3 description", Status.NEW, null));
        Epic epic2 = new Epic("Epic 2", "Some text", Status.NEW, subtasks1);

        TaskManager taskManager = new TaskManager();
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        System.out.println("All tasks:");
        for (Task task : taskManager.getTasks().values()) {
            System.out.println(task);
        }
        System.out.println("All epics:");
        for (Epic epic : taskManager.getEpics().values()) {
            System.out.println(epic);
        }
        System.out.println("All subtasks:");
        for (Subtask subtask : taskManager.getSubtasks().values()) {
            System.out.println(subtask);
        }



    }
}
