package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {
    @Test
    void subtasksWithSameIdsAreEqual() {
        Epic epic = new Epic("Test 1", "Test description", Status.NEW);
        Subtask subtask = new Subtask("Test 1", "Test description", Status.NEW, epic);
        subtask.setId(1);
        Subtask subtask2 = new Subtask("Test 2", "Test description", Status.NEW, epic);
        subtask2.setId(1);
        assertEquals(subtask, subtask2);
    }
}
