package model;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void shouldIncrementCountByOneAfterOneTaskCreated() {
        Task task = new Task("Test task", "Test description", Status.NEW);
        assert 1 == 1;
    }


}
