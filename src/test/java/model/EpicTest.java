package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    @Test
    public void epicsWithSameIdsAreEqual() {
        Epic epic = new Epic("Test 1", "Test description", Status.NEW);
        epic.setId(1);
        Epic epic2 = new Epic("Test 2", "Test description", Status.NEW);
        epic2.setId(1);
        assertEquals(epic, epic2);
    }

    @Test
    public void epicDurationIsSumOfSubtasks() {
        Epic epic = new Epic("Test 1", "Test description", Status.NEW);
        epic.setId(1);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(1);

        Subtask subtask1 = new Subtask("Test 1", "Test description", Status.NEW, epic.getId(), 10, now);
        Subtask subtask2 = new Subtask("Test 2", "Test description", Status.NEW, epic.getId(), 20, later);

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        assertEquals(30, epic.getDuration().toMinutes());
    }

    @Test
    public void epicDurationIsZeroWhenNoSubtasks() {
        Epic epic = new Epic("Test 1", "Test description", Status.NEW);

        assertEquals(0, epic.getDuration().toMinutes());
    }

    @Test
    public void epicStartTimeIsEarliestSubtaskStartTime() {
        Epic epic = new Epic("Test 1", "Test description", Status.NEW);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(1);

        Subtask subtask1 = new Subtask("Test 1", "Test description", Status.NEW, epic.getId(), 10, now);
        Subtask subtask2 = new Subtask("Test 2", "Test description", Status.NEW, epic.getId(), 20, later);

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        assertEquals(now, epic.getStartTime());
    }
}
