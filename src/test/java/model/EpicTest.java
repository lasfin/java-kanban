package model;

import org.junit.jupiter.api.Test;

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
}
