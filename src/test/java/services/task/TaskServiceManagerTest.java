package services.task;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.Managers;
import services.exeptions.TaskServiceValidationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceManagerTest {
    TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskService = Managers.getDefault();
    }

    @Test
    public void shouldReturnDefaultTaskService() {
        assertNotNull(taskService);
    }

    @Test
    public void firstTasksAddedWithRightId() {
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a new task for the project", Status.NEW);
        taskService.addTask(task1);
        taskService.addTask(task2);

        assertEquals(task1.getId(), 1);
        assertEquals(task2.getId(), 2);
    }

    @Test
    public void shouldBeAbleToAddDifferentTasks() {
        Task task1 = new Task("Create a first task", "Create a new task for the project", Status.NEW);
        Task task2 = new Task("Create a second task", "Create a second task for the project", Status.NEW);
        taskService.addTask(task1);
        taskService.addTask(task2);
        assertNotNull(taskService.getTask(task1.getId()));
        assertNotNull(taskService.getTask(task2.getId()));
    }

    @Test
    public void epicShouldChangeStatusWhenSubtaskStatusChanged() {
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
    public void epicShouldChangeStatusWhenSubtaskDeleted() {
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.IN_PROGRESS, epic1.getId());
        taskService.addSubtask(subtask1);

        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
        taskService.removeSubtask(subtask1);
        assertEquals(epic1.getStatus(), Status.NEW);
    }


    @Test
    public void allSubtasksTasksShouldBeDeletedWhenEpicDeleted() {
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
    public void allTasksDeletedAfterDeleteAllCalled() {
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
    public void testEpicWithNewAndInProgress() {
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.IN_PROGRESS, epic1.getId());
        taskService.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", Status.NEW, epic1.getId());
        taskService.addSubtask(subtask2);

        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void testEpicWithDoneAndNew() {
        Epic epic1 = new Epic("Epic 1", "Create a new epic for the project", Status.NEW);
        taskService.addEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", Status.DONE, epic1.getId());
        taskService.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", Status.NEW, epic1.getId());
        taskService.addSubtask(subtask2);

        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void shouldGetPrioritizedTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(1);
        LocalDateTime evenLater = now.plusDays(2);

        Task task1 = new Task("test", "test", Status.NEW, 10, now);
        task1.setId(1);
        Task task2 = new Task("test", "test", Status.NEW, 5, later);
        task2.setId(2);
        Task task3 = new Task("test", "test", Status.NEW, 15, evenLater);
        task3.setId(3);

        taskService.addTask(task3);
        taskService.addTask(task1);
        taskService.addTask(task2);

        assertEquals(taskService.getPrioritizedTasks().get(0), task1);
    }

    @Test
    public void taskWithoutStartTimeShouldNotBeInList() {
        taskService.addTask(new Task("test", "test", Status.NEW));
        taskService.addTask(new Task("test", "test", Status.NEW));

        assertEquals(taskService.getPrioritizedTasks().size(), 0);
    }

    @Test
    public void tasksWithOverlapsShouldNotExist() {
        LocalDateTime now = LocalDateTime.now();
        Task task1 = new Task("test", "test", Status.NEW, 20, now);
        Task task2 = new Task("test", "test", Status.NEW, 20, now);

        taskService.addTask(task1);

        Assertions.assertThrows(TaskServiceValidationException.class, () -> {
            taskService.addTask(task2);
        });
    }
}
