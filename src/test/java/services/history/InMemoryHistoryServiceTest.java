package services.history;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryHistoryServiceTest {
    @Test
    public void shouldAddTaskToHistory() {
        InMemoryHistoryService historyService = new InMemoryHistoryService();
        Task task = new Task("test", "test", Status.NEW);
        historyService.add(task);

        Assertions.assertTrue(historyService.getHistory().contains(task));
    }

    @Test
    public void shouldNotExceedHistoryLimit() {
        InMemoryHistoryService historyService = new InMemoryHistoryService();

        Task task = new Task("test", "test", Status.NEW);
        for (int i = 0; i <= 10 + 2; i++) {
            historyService.add(task);
        }

        Assertions.assertEquals(10, historyService.getHistory().size());
    }

    @Test
    public void shouldReturnEmptyHistory() {
        InMemoryHistoryService historyService = new InMemoryHistoryService();
        Assertions.assertTrue(historyService.getHistory().isEmpty());
    }

    @Test
    void shouldRemoveFirstTaskWhenExceedingHistoryLimit() {
        InMemoryHistoryService historyService = new InMemoryHistoryService();

        Task task = new Task("first", "second", Status.NEW);
        task.setId(1);
        Task task2 = new Task("second", "second", Status.IN_PROGRESS);
        task2.setId(2);

        historyService.add(task);

        for (int i = 0; i <= 10; i++) {
            historyService.add(task2);
        }

        Assertions.assertFalse(historyService.getHistory().contains(task));
        Assertions.assertTrue(historyService.getHistory().contains(task2));
        Assertions.assertEquals(10, historyService.getHistory().size());
    }
}
