package services.task;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import services.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskServiceManagerTest {
    @Test
    public void shouldReturnDefaultTaskService() {
        TaskService taskService = Managers.getDefault();
        assertNotNull(taskService);
    }

    @Test
    void firstTasksAddedWithRightId() {
        TaskService taskService = Managers.getDefault();
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a new task for the project", Status.NEW);
        taskService.addTask(task1);
        taskService.addTask(task2);

        assertEquals(task1.getId(), 1);
        assertEquals(task2.getId(), 2);
    }

    @Test
    public void shouldBeAbleToAddDifferentTasks() {
        TaskService taskService = Managers.getDefault();
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a second task for the project", Status.NEW);
        taskService.addTask(task1);
        taskService.addTask(task2);
        assertNotNull(taskService.getTask(task1.getId()));
        assertNotNull(taskService.getTask(task2.getId()));
    }

    @Test
    public void epicShouldChangeStatusWhenSubtaskStatusChanged() {
        TaskService taskService = Managers.getDefault();
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.NEW, epic1.getId());
        taskService.addSubtask(subtask1);

        assertEquals(epic1.getStatus(), Status.NEW);
        subtask1.setStatus(Status.IN_PROGRESS);
        taskService.updateSubtask(subtask1);
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void epicShouldChangeStatusWhenSubtaskDeleted() {
        TaskService taskService = Managers.getDefault();
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.IN_PROGRESS, epic1.getId());
        taskService.addSubtask(subtask1);

        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
        taskService.removeSubtask(subtask1);
        assertEquals(epic1.getStatus(), Status.NEW);
    }


    @Test
    void allSubtasksTasksShouldBeDeletedWhenEpicDeleted() {
        TaskService taskService = Managers.getDefault();
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.IN_PROGRESS, epic1.getId());
        taskService.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", Status.IN_PROGRESS, epic1.getId());
        taskService.addSubtask(subtask2);

        assertEquals(taskService.getSubtasks().size(), 2);

        taskService.removeEpic(epic1);
        assertEquals(taskService.getSubtasks().size(), 0);
    }

    @Test
    void allTasksDeletedAfterDeleteAllCalled() {
        TaskService taskService = Managers.getDefault();
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        taskService.addTask(task1);
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.IN_PROGRESS, epic1.getId());
        taskService.addSubtask(subtask1);

        taskService.removeAll();
        assertEquals(taskService.getTasks().size(), 0);
        assertEquals(taskService.getEpics().size(), 0);
        assertEquals(taskService.getSubtasks().size(), 0);
    }

    @Test
    void testEpicWithNewAndInProgress() {
        TaskService taskService = Managers.getDefault();
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.IN_PROGRESS, epic1.getId());
        taskService.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", Status.NEW, epic1.getId());
        taskService.addSubtask(subtask2);

        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void testEpicWithDoneAndNew() {
        TaskService taskService = Managers.getDefault();
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.DONE, epic1.getId());
        taskService.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", Status.NEW, epic1.getId());
        taskService.addSubtask(subtask2);

        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
    }
}
