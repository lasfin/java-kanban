import service.TaskService;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskService taskManager = new TaskService();
        // Create two new tasks
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a second task for the project", Status.NEW);

        // Create a new epic (with 2 subtask)
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        epic1.addSubtask(new Subtask("Subtask 1", "Subtask 1 description", Status.NEW, epic1));
        epic1.addSubtask(new Subtask("Subtask 2", "Subtask 2 description", Status.NEW, epic1));

        // create a new epic (with 1 subtask)
        Epic epic2 = new Epic("Epic 2", "Some text", Status.NEW);
        epic2.addSubtask(new Subtask("Subtask 3", "Subtask 3 description", Status.NEW, epic2));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        printAllTasks(taskManager);

        int idToUpdate = 7;
        int idEpic = 6;
        int idEpicToDelete = 3;

        System.out.println("Старый статус эпика: " + taskManager.getEpic(idEpic).getStatus());

        // меняем статус 7 сабтаски, он должен измениться в эпике
        Task taskToUpdate = taskManager.getSubtask(idToUpdate);
        taskToUpdate.setStatus(Status.DONE);
        taskManager.updateTask(taskToUpdate);

        System.out.println("Новый статус сабзадачи: " + taskManager.getSubtask(idToUpdate).getStatus());
        System.out.println("Новый статус эпика: " + taskManager.getEpic(idEpic).getStatus());

        // удаляем сабтаску, она должна удалиться из эпика, эпик переходит в статус NEW
        taskManager.removeTask(idToUpdate);
        System.out.println("Статус эпика после удаления единственной сабтаски: "
                + taskManager.getEpic(idEpic).getStatus() + "\n");

        // удаляем эпик, все сабтаски должны удалиться (всего 2 задачи, 1 эпик без сабтасок)
        taskManager.removeTask(idEpicToDelete);
        printAllTasks(taskManager);

        // удаляем все таски
        taskManager.removeAll();
        printAllTasks(taskManager);

        testEpicWithNewAndInProgress();
        testEpicWithExtraSubtaskAdded();
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

    public static void testEpicWithNewAndInProgress () {
        /**
         * Если одна подзадача в статусе DONE, а остальные NEW, эпик должен быть в статусе IN_PROGRESS
         */
        TaskService taskManager = new TaskService();

        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        epic1.addSubtask(new Subtask("Subtask 1", "Subtask 1 description", Status.NEW, epic1));
        epic1.addSubtask(new Subtask("Subtask 2", "Subtask 2 description", Status.DONE, epic1));

        taskManager.addEpic(epic1);

        System.out.println("Epic should have status IN_PROGRESS: " + epic1.getStatus());
    }

    public static void testEpicWithExtraSubtaskAdded () {
        /**
         * При создании подзадачи, ее идентификатор должен быть добавлен в список в эпике,
         * а также должен быть обновлен статус эпика
         */
        TaskService taskManager = new TaskService();
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);

        epic1.addSubtask(new Subtask("Subtask 1", "Subtask 1 description", Status.DONE, epic1));
        taskManager.addEpic(epic1);

        System.out.println("Epic should have status DONE: " + epic1.getStatus());

        /**
         * Удаляем подзадачу, статус эпика должен измениться на NEW
         */
        taskManager.removeTask(2);
        System.out.println("Epic should have status NEW: " + epic1.getStatus());
    }
}

// todo: вынести addSubtask в taskService