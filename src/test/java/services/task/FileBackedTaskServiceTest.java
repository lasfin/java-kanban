package services.task;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.exeptions.TaskServiceValidationException;
import services.exeptions.TasksServiceSaveException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskServiceTest {
    FileBackedTaskService fileBackedTaskService;

    @BeforeEach
    public void setUp() {
        fileBackedTaskService = new FileBackedTaskService();
    }

    @AfterEach
    public void tearDown() {
        fileBackedTaskService.removeAll();
    }

    @Test
    public void shouldAddOneTask() {
        Task task = new Task("test3", "test add 3", Status.NEW);
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
            Assertions.fail("Should not throw exception" + e.getMessage());
        }
    }

    @Test
    public void shouldLoadTasksFromExternalFile() {
        String fileName = "temp.csv";
        try {
            File tempFile = File.createTempFile("fileName", ".csv");

            try (var writer = Files.newBufferedWriter(tempFile.toPath(), java.nio.charset.StandardCharsets.UTF_8)) {
                writer.write("1,task,test,NEW,test task,none,none,\n");
                writer.write("2,epic,test,NEW,test external file epic2,none,none,\n");
                writer.write("3,subtask,test,NEW,test subtask,none,none,2,\n");
                writer.write("5,task,test,NEW,test task5,100,03-Nov-24-01:06,\n"); // task with time
                writer.write("6,subtask,test,NEW,test subtask6,100,04-Nov-21-02:05,2,\n"); // subtask with time
            } catch (Exception e) {
                Assertions.fail("Should not throw exception: " + e.getCause() + e.getMessage());
            }
            fileBackedTaskService = new FileBackedTaskService(tempFile);

            Assertions.assertEquals(2, fileBackedTaskService.getTasks().size());
            Assertions.assertEquals(1, fileBackedTaskService.getEpics().size());
            Assertions.assertEquals(2, fileBackedTaskService.getSubtasks().size());
        } catch (Exception e) {
            Assertions.fail("Should not throw exception");
        } finally {
            if (Files.exists(new File(fileName).toPath())) {
                new File(fileName).delete();
            }
        }
    }

    @Test
    public void shouldThrowCustomExceptionWhenFileNotFound() {
        Path notFoundPath = Path.of("notfound.csv");
        Assertions.assertThrows(TasksServiceSaveException.class, () -> {
            fileBackedTaskService.loadFromFile(notFoundPath);
        });
    }

    @Test
    public void shouldLoadExternalFileByStaticMethod() {
        String fileName = "temp.csv";
        try {
            File tempFile = File.createTempFile("fileNameForFileTwo", ".csv");

            try (var writer = Files.newBufferedWriter(tempFile.toPath(), java.nio.charset.StandardCharsets.UTF_8)) {
                writer.write("1,task,test,NEW,test task,none,none\n");
                writer.write("2,epic,test,NEW,test external file epic2,none,none\n");
                writer.write("7,epic,test,NEW,test external file epic7,none,none\n");
                writer.write("3,subtask,test,NEW,test subtask,none,none,2\n");
                writer.write("4,subtask,test,NEW,test subtask,none,none,2\n");
            } catch (Exception e) {
                Assertions.fail("Should not throw exception " + e.getMessage());
            }

            fileBackedTaskService = FileBackedTaskService.loadFromFile(tempFile);
            Assertions.assertEquals(1, fileBackedTaskService.getTasks().size());
            Assertions.assertEquals(2, fileBackedTaskService.getEpics().size());
            Assertions.assertEquals(2, fileBackedTaskService.getSubtasks().size());
            for (Epic epic : fileBackedTaskService.getEpics()) {
                System.out.println(epic);
            }
        } catch (IOException e) {
            Assertions.fail("Should not throw IOException " + e.getMessage());
        } finally {
            if (Files.exists(new File(fileName).toPath())) {
                new File(fileName).delete();
            }
        }
    }


    @Test
    public void shouldBeAbleHandleNotNoneTime() {
        Task task = new Task("test", "test time", Status.NEW);
        fileBackedTaskService.addTask(task);
        Epic epic = new Epic("test", "test time epic", Status.NEW);
        fileBackedTaskService.addEpic(epic);
        Subtask subtask = new Subtask("test", "test time subtask", Status.NEW, epic.getId(), 100, java.time.LocalDateTime.now());
        fileBackedTaskService.addSubtask(subtask);


        Assertions.assertEquals(100, fileBackedTaskService.getSubtasks().get(0).getDuration().toMinutes());
        Assertions.assertEquals(100, fileBackedTaskService.getEpics().get(0).getDuration().toMinutes());
    }

    @Test
    public void shouldSaveTasksOfAllTypesWithAllFields() {
        Task task = new Task("test", "test all", Status.NEW, 100, java.time.LocalDateTime.now().plusDays(2));
        fileBackedTaskService.addTask(task);
        Epic epic = new Epic("test", "test all epic", Status.NEW);
        fileBackedTaskService.addEpic(epic);
        Subtask subtask = new Subtask("test", "test all subtask", Status.NEW, epic.getId(), 100, java.time.LocalDateTime.now());
        fileBackedTaskService.addSubtask(subtask);

        Assertions.assertEquals(1, fileBackedTaskService.getTasks().size());
        Assertions.assertEquals(1, fileBackedTaskService.getEpics().size());
        Assertions.assertEquals(1, fileBackedTaskService.getSubtasks().size());
    }

    @Test
    public void subtaskShouldThrowExceptionOnOverlap() {
        Task task = new Task("test", "test all", Status.NEW, 100, java.time.LocalDateTime.now());
        fileBackedTaskService.addTask(task);
        Epic epic = new Epic("test", "test all epic", Status.NEW);
        fileBackedTaskService.addEpic(epic);

        Subtask subtask = new Subtask("test", "test all subtask", Status.NEW, epic.getId(), 100, java.time.LocalDateTime.now());

        Assertions.assertThrows(TaskServiceValidationException.class, () -> {
            fileBackedTaskService.addSubtask(subtask);
        });
    }
}
