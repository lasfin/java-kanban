import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        // Create two new tasks
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a second task for the project", Status.NEW);

        // Create a new epic (with 2 subtask)
        Subtask[] tasks = new Subtask[2];
        Epic epic1 = new Epic("Create a new epic", "Create a new epic for the project", Status.NEW, tasks);
        tasks[0] = new Subtask("Task 1", "Task 1 description", Status.NEW, epic1);
        tasks[1] = new Subtask("Task 2", "Task 2 description", Status.IN_PROGRESS, epic1);

        // create a new epic (with 1 subtask)
        Subtask[] tasks1 = new Subtask[1];
        Epic epic2 = new Epic("Create a new epic", "Create a new epic for the project", Status.NEW, tasks1);
        tasks1[0] = new Subtask("Task 1", "Task 1 description", Status.NEW, epic2);

        TaskManager taskManager = new TaskManager();
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        for (Task task : taskManager.getTasks().values()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getEpics().values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks().values()) {
            System.out.println(subtask);
        }

    }
}
