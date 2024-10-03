package services.history;

import model.Epic;
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
    public void shouldRemoveTaskFromHistory() {
        InMemoryHistoryService historyService = new InMemoryHistoryService();
        Task task = new Task("test", "test", Status.NEW);
        task.setId(1);
        Epic epic = new Epic("test", "test", Status.NEW);
        epic.setId(2);

        historyService.add(task);
        historyService.add(epic);

        Assertions.assertTrue(historyService.getHistory().contains(task));
        Assertions.assertTrue(historyService.getHistory().contains(epic));

        historyService.remove(task.getId());
        historyService.remove(epic.getId());

        Assertions.assertFalse(historyService.getHistory().contains(task));
        Assertions.assertFalse(historyService.getHistory().contains(epic));
    }

    @Test
    public void shouldBeAbleStoreManyTasksInHistory() {
        InMemoryHistoryService historyService = new InMemoryHistoryService();

        for (int i = 0; i < 13; i++) {
            Task task = new Task("test", "test", Status.NEW);
            task.setId(i);
            historyService.add(task);
        }

        Assertions.assertEquals(13, historyService.getHistory().size());
    }

    @Test
    public void shouldReturnEmptyHistory() {
        InMemoryHistoryService historyService = new InMemoryHistoryService();

        Assertions.assertTrue(historyService.getHistory().isEmpty());
    }

    @Test
    public void shouldReplaceSameTask() {
        InMemoryHistoryService historyService = new InMemoryHistoryService();
        Task task = new Task("test", "test", Status.NEW);
        task.setId(1);
        historyService.add(task);
        task.setStatus(Status.IN_PROGRESS);
        historyService.add(task);

        Assertions.assertEquals(1, historyService.getHistory().size());
    }
}
