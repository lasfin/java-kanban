import service.TaskService;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

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

        TaskService taskManager = new TaskService();
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);


        printAllTasks(taskManager);

        // меняем статус 7 сабтаски, он должен измениться в эпике
        System.out.println("Старый статус эпика: " + taskManager.getEpic(6).getStatus());
        taskManager.getSubtask(7).setStatus(Status.DONE);
        System.out.println("Новый статус сабзадачи: " + taskManager.getSubtask(7).getStatus());
        System.out.println("Новый статус эпика: " + taskManager.getEpic(6).getStatus());

        // удаляем сабтаску, она должна удалиться из эпика, эпик переходит в статус NEW
        taskManager.removeTask(7);
        System.out.println("Статус эпика после удаления единственной сабтаски: "
                + taskManager.getEpic(6).getStatus() + "\n");

        // удаляем эпик, все сабтаски должны удалиться (всего 2 задачи, 1 эпик без сабтасок)
        taskManager.removeTask(3);

        printAllTasks(taskManager);

        // удаляем все таски
        taskManager.removeAll();
        printAllTasks(taskManager);
    }

    public static void printAllTasks(TaskService taskManager) {
        if (taskManager.getTasks().isEmpty() &&
            taskManager.getEpics().isEmpty() &&
            taskManager.getSubtasks().isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

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
        System.out.println();
    }
}
