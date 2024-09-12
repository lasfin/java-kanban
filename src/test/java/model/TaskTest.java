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
}
