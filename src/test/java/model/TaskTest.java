package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {

    @Test
    public void tasksWithSameIdsAreEqual() {
        Task task = new Task("Test 1", "Test description", Status.NEW);
        task.setId(1);
        Task task2 = new Task("Test 2", "Test description", Status.NEW);
        task2.setId(1);
        assertEquals(task, task2);
    }

    @Test
    public void tasksWithDifferentIdsAreNotEqual() {
        Task task = new Task("Test 1", "Test description", Status.NEW);
        task.setId(1);
        Task task2 = new Task("Test 2", "Test description", Status.NEW);
        task2.setId(2);
        assertNotEquals(task, task2);
    }

    @Test
    public void epicsWithSameIdsAreEqual() {
        Epic epic = new Epic("Test 1", "Test description", Status.NEW);
        epic.setId(1);
        Epic epic2 = new Epic("Test 2", "Test description", Status.NEW);
        epic2.setId(1);
        assertEquals(epic, epic2);
    }

    @Test void subtasksWithSameIdsAreEqual() {
        Epic epic = new Epic("Test 1", "Test description", Status.NEW);
        Subtask subtask = new Subtask("Test 1", "Test description", Status.NEW, epic);
        subtask.setId(1);
        Subtask subtask2 = new Subtask("Test 2", "Test description", Status.NEW, epic);
        subtask2.setId(1);
        assertEquals(subtask, subtask2);
    }


}
