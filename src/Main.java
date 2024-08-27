import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        // Create a new task
        Task task = new Task("Create a new task", "Create a new task for the project", Status.NEW);
        System.out.println("Task Name: " + task.getName());
        System.out.println("Task Description: " + task.getDescription());
        System.out.println("Task Status: " + task.getStatus());

        // Create a new epic
        Task[] tasks = new Task[3];
        tasks[0] = new Task("Task 1", "Task 1 description", Status.NEW);
        tasks[1] = new Task("Task 2", "Task 2 description", Status.IN_PROGRESS);
        tasks[2] = new Task("Task 3", "Task 3 description", Status.DONE);
        Epic epic = new Epic("Create a new epic", "Create a new epic for the project", Status.NEW, tasks);
        System.out.println("Epic Name: " + epic.getName());
        System.out.println("Epic Description: " + epic.getDescription());
        System.out.println("Epic Status: " + epic.getStatus());

        // Create a new subtask
        Subtask subtask = new Subtask("Create a new subtask", "Create a new subtask for the project", Status.IN_PROGRESS, task);
        System.out.println("Subtask Name: " + subtask.getName());
        System.out.println("Subtask Description: " + subtask.getDescription());
        System.out.println("Subtask Status: " + subtask.getStatus());
        System.out.println("Subtask Parent Task: " + subtask.getParentTask().getName());


        // Create a new task manager
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
    }
}
