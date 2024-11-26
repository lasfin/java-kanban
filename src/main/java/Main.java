import api.HttpTaskServer;
import services.Managers;
import services.task.TaskService;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        TaskService taskService = Managers.getDefault();

        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a second task for the project", Status.NEW);
        taskService.addTask(task1);
        taskService.addTask(task2);

        HttpTaskServer.main(args, taskService, 8080);
    }

    public static void runGeneralFlow() {
        TaskService taskManager = Managers.getDefault();
        // Create two new tasks
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a second task for the project", Status.NEW);
        // Create a new epic
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", Status.NEW, epic1.getId());

        // create a new epic
        Epic epic2 = new Epic("Epic 2", "Some text", Status.NEW);
        taskManager.addEpic(epic2);

        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", Status.NEW, epic2.getId());

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        printAllTasks(taskManager);

        int idSecondEpic = epic2.getId();

        taskManager.getEpic(idSecondEpic);
        System.out.println("History of views (should be one epic here): " + taskManager.getHistory());

        System.out.println("Old epic status should be NEW: " + taskManager.getEpic(idSecondEpic).getStatus());

        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);

        System.out.println("Updated subtask status is: " + taskManager.getSubtask(subtask3.getId()).getStatus());
        System.out.println("Epic should have status IN_PROGRESS: " + taskManager.getEpic(idSecondEpic).getStatus());

        taskManager.removeSubtask(subtask3);
        System.out.println("Deleted the only one subtask, Epic should have status NEW: "
                + taskManager.getEpic(idSecondEpic).getStatus() + "\n");

        taskManager.removeEpic(epic2);
        printAllTasks(taskManager);

        taskManager.getEpic(idSecondEpic);
        taskManager.getTask(task1.getId());

        taskManager.removeAll();

        int allTasksSize =
                taskManager.getTasks().size() +
                taskManager.getEpics().size() +
                taskManager.getSubtasks().size();

        System.out.println("All tasks deleted, size should be zero: " + allTasksSize);
    }

    public static void printAllTasks(TaskService taskManager) {
        if (taskManager.getTasks().isEmpty() &&
                taskManager.getEpics().isEmpty() &&
                taskManager.getSubtasks().isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("All tasks:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("All epics:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("All subtasks:");
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println();
    }
}
