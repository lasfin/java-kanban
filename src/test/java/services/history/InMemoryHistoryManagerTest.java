package services.history;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryHistoryManagerTest {
    @Test
    public void shouldAddTaskToHistory() {
        InMemoryHistoryHistoryManager historyManager = new InMemoryHistoryHistoryManager(10);
        Task task = new Task("test", "test", Status.NEW);
        historyManager.add(task);

        // Add an assertion to verify that the task was added to the history
        Assertions.assertTrue(historyManager.getHistory().contains(task));
    }

    @Test
    public void shouldNotExceedHistoryLimit() {
        int lastTasksCap = 10;
        InMemoryHistoryHistoryManager historyManager = new InMemoryHistoryHistoryManager(lastTasksCap);

        Task task = new Task("test", "test", Status.NEW);
        for (int i = 0; i <= lastTasksCap + 2; i++) {
            historyManager.add(task);
        }

        // Add an assertion to verify that the history size does not exceed 10
        Assertions.assertEquals(lastTasksCap, historyManager.getHistory().size());
    }

    @Test
    public void shouldReturnEmptyHistory() {
        InMemoryHistoryHistoryManager historyManager = new InMemoryHistoryHistoryManager(10);
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test void shouldRemoveFirstTaskWhenExceedingHistoryLimit() {
        int lastTasksCap = 1;
        InMemoryHistoryHistoryManager historyManager = new InMemoryHistoryHistoryManager(lastTasksCap);

        Task task = new Task("first", "second", Status.NEW);
        task.setId(1);
        Task task2 = new Task("second", "second", Status.IN_PROGRESS);
        task2.setId(2);

        historyManager.add(task);
        historyManager.add(task2);

        Assertions.assertFalse(historyManager.getHistory().contains(task));
        Assertions.assertTrue(historyManager.getHistory().contains(task2));
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }
}
