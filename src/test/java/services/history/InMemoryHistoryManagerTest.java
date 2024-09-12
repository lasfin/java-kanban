package services.history;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryHistoryManagerTest {
    @Test
    public void shouldAddTaskToHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("test", "test", Status.NEW);
        historyManager.add(task);

        Assertions.assertTrue(historyManager.getHistory().contains(task));
    }

    @Test
    public void shouldNotExceedHistoryLimit() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task = new Task("test", "test", Status.NEW);
        for (int i = 0; i <= 10 + 2; i++) {
            historyManager.add(task);
        }

        Assertions.assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    public void shouldReturnEmptyHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldRemoveFirstTaskWhenExceedingHistoryLimit() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task = new Task("first", "second", Status.NEW);
        task.setId(1);
        Task task2 = new Task("second", "second", Status.IN_PROGRESS);
        task2.setId(2);

        historyManager.add(task);

        for (int i = 0; i <= 10; i++) {
            historyManager.add(task2);
        }

        Assertions.assertFalse(historyManager.getHistory().contains(task));
        Assertions.assertTrue(historyManager.getHistory().contains(task2));
        Assertions.assertEquals(10, historyManager.getHistory().size());
    }
}
