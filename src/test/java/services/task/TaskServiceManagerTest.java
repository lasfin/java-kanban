package services.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskServiceManagerTest {
    @Test
    public void shouldReturnDefaultTaskService() {
        TaskService taskService = TaskServiceManager.getDefault();
        assertNotNull(taskService);
    }
}
