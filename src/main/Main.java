package main;

import main.service.InMemoryTaskService;
import main.service.TaskServiceManager;
import main.service.TaskService;
import main.model.Epic;
import main.model.Status;
import main.model.Subtask;
import main.model.Task;

public class Main {

    public static void main(String[] args) {
        testGeneralFlow();
        testEpicWithNewAndInProgress();
        testEpicWithExtraSubtaskAdded();
        testUpdateSubtask();
    }

    public static void testGeneralFlow() {
        TaskService taskManager = TaskServiceManager.getDefault();
        // Create two new tasks
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a second task for the project", Status.NEW);
        // Create a new epic (with 2 subtask)
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.NEW, epic1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", Status.NEW, epic1);
        // create a new epic (with 1 subtask)
        Epic epic2 = new Epic("Epic 2", "Some text", Status.NEW);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", Status.NEW, epic2);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        printAllTasks(taskManager);

        int idSecondEpic = epic2.getId();

        System.out.println("Old epic status should be NEW: " + taskManager.getEpic(idSecondEpic).getStatus());

        // меняем статус сабтаски, он должен измениться в эпике
        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);

        System.out.println("Updated subtask status is: " + taskManager.getSubtask(subtask3.getId()).getStatus());
        System.out.println("Epic should have status IN_PROGRESS: " + taskManager.getEpic(idSecondEpic).getStatus());

        // удаляем сабтаску, она должна удалиться из эпика, эпик переходит в статус NEW
        taskManager.removeSubtask(subtask3);
        System.out.println("Deleted the only one subtask, Epic should have status NEW: "
                + taskManager.getEpic(idSecondEpic).getStatus() + "\n");

        // удаляем эпик, все сабтаски должны удалиться (всего 2 задачи, 1 эпик без сабтасок)
        taskManager.removeEpic(epic2);
        printAllTasks(taskManager);

        // удаляем все таски
        taskManager.removeAll();
        int allTasksSize =
                taskManager.getTasks().size() +
                taskManager.getEpics().size() +
                taskManager.getSubtasks().size();
        System.out.println("All tasks deleted, size should be zero: " + allTasksSize);
    }

    public static void testEpicWithNewAndInProgress () {
        /**
         * Если одна подзадача в статусе DONE, а остальные NEW, эпик должен быть в статусе IN_PROGRESS
         */
        InMemoryTaskService taskManager = new InMemoryTaskService();

        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);

        taskManager.addSubtask(new Subtask("Subtask 1", "Subtask 1 description", Status.NEW, epic1));
        taskManager.addSubtask(new Subtask("Subtask 2", "Subtask 2 description", Status.DONE, epic1));
        taskManager.addEpic(epic1);

        System.out.println("Epic should have status IN_PROGRESS: " + epic1.getStatus());
    }

    public static void testEpicWithExtraSubtaskAdded () {
        /**
         * При создании подзадачи, ее идентификатор должен быть добавлен в список в эпике,
         * а также должен быть обновлен статус эпика
         */
        InMemoryTaskService taskManager = new InMemoryTaskService();
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        Subtask subtask = new Subtask("Subtask 1", "Subtask 1 description", Status.DONE, epic1);
        taskManager.addSubtask(subtask);
        taskManager.addEpic(epic1);

        System.out.println("Epic should have status DONE: " + epic1.getStatus());

        /**
         * Удаляем подзадачу, статус эпика должен измениться на NEW
         */
        taskManager.removeSubtask(subtask);
        System.out.println("Epic should have status NEW: " + epic1.getStatus());
    }

    public static void testUpdateSubtask() {
        /**
         * При обновлении статуса подзадачи, статус эпика должен быть обновлен
         */
        InMemoryTaskService taskManager = new InMemoryTaskService();
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.NEW, epic1);


        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);

        System.out.println("Epic should have status TASK IN_PROGRESS: " + taskManager.getEpic(1).getStatus());
        System.out.println("Subtask should have status IN_PROGRESS: " + taskManager.getSubtask(2).getStatus());
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
