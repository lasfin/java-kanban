package services.history;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.task.FileBackedTaskService;

import java.io.File;
import java.nio.file.Files;

public class FileBackedTaskServiceTest {
    FileBackedTaskService fileBackedTaskService;

    @BeforeEach
    public void setUp() {
        fileBackedTaskService = new FileBackedTaskService();
        fileBackedTaskService.removeAll();
    }

    @Test
    public void shouldAddOneTask() {
        Task task = new Task("test", "test add one", Status.NEW);
        fileBackedTaskService.addTask(task);

        Assertions.assertEquals(1, fileBackedTaskService.getTasks().size());
    }

    @Test
    public void possibleToCallRemoveAllTwice() {
        Task task = new Task("test", "test remove all twice", Status.NEW);
        fileBackedTaskService.addTask(task);

        Assertions.assertEquals(1, fileBackedTaskService.getTasks().size());
        fileBackedTaskService.removeAll();
        fileBackedTaskService.removeAll();
        Assertions.assertEquals(0, fileBackedTaskService.getTasks().size());
    }

    @Test
    public void shouldRemoveAllTasks() {
        Task task = new Task("test", "test remove", Status.NEW);
        fileBackedTaskService.addTask(task);
        fileBackedTaskService.removeAll();

        Assertions.assertEquals(0, fileBackedTaskService.getTasks().size());
    }

    @Test
    public void shouldSaveTasksOfAllTypes() {
        Task task = new Task("test", "test all", Status.NEW);
        fileBackedTaskService.addTask(task);
        Epic epic = new Epic("test", "test all epic", Status.NEW);
        fileBackedTaskService.addEpic(epic);
        Subtask subtask = new Subtask("test", "test all subtask", Status.NEW, epic.getId());
        fileBackedTaskService.addSubtask(subtask);

        Assertions.assertEquals(1, fileBackedTaskService.getTasks().size());
        Assertions.assertEquals(1, fileBackedTaskService.getEpics().size());
        Assertions.assertEquals(1, fileBackedTaskService.getSubtasks().size());
    }

    @Test
    public void shouldBeAbleUseExternalFIle() {
        try {
            File tempFile = File.createTempFile("temp", ".csv");
            fileBackedTaskService = new FileBackedTaskService(tempFile);
            Task task = new Task("test", "test external file", Status.NEW);
            fileBackedTaskService.addTask(task);
            Epic epic = new Epic("test", "test external file epic", Status.NEW);
            fileBackedTaskService.addEpic(epic);
            Subtask subtask = new Subtask("test", "test external file subtask", Status.NEW, epic.getId());
            fileBackedTaskService.addSubtask(subtask);

            Assertions.assertEquals(1, fileBackedTaskService.getTasks().size());
            Assertions.assertEquals(1, fileBackedTaskService.getEpics().size());
            Assertions.assertEquals(1, fileBackedTaskService.getSubtasks().size());
        } catch (Exception e) {
            Assertions.fail("Should not throw exception");
        }
    }

    @Test
    public void shouldLoadTasksFromExternalFile() {
        String fileName = "temp.csv";
        try {
            File tempFile = File.createTempFile("fileName", ".csv");

            try (var writer = Files.newBufferedWriter(tempFile.toPath(), java.nio.charset.StandardCharsets.UTF_8)) {
                writer.write("1,task,test,NEW,test task\n");
                writer.write("2,epic,test,NEW,test external file epic\n");
                writer.write("3,subtask,test,NEW,test subtask,2\n");
            } catch (Exception e) {
                Assertions.fail("Should not throw exception");
            }
            fileBackedTaskService = new FileBackedTaskService(tempFile);

            Assertions.assertEquals(1, fileBackedTaskService.getTasks().size());
            Assertions.assertEquals(1, fileBackedTaskService.getEpics().size());
            Assertions.assertEquals(1, fileBackedTaskService.getSubtasks().size());
        } catch (Exception e) {
            Assertions.fail("Should not throw exception");
        } finally {
            if (Files.exists(new File(fileName).toPath())) {
                new File(fileName).delete();
            }
        }
    }
}
