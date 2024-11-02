package services.task;

import model.*;
import services.exeptions.TaskServiceNotFoundException;
import services.exeptions.TasksServiceSaveException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FileBackedTaskService extends InMemoryTaskService {
    private final Path filePath;

    public FileBackedTaskService() {
        super();
        String defaultFilenamePath = "src/main/java/resources/tasks.csv";
        filePath = Path.of(defaultFilenamePath);

        loadOrNotify(filePath);
    }

    public FileBackedTaskService(File file) {
        super();
        filePath = file.toPath();
        loadOrNotify(filePath);
    }

    public static FileBackedTaskService loadFromFile(File file) {
        return new FileBackedTaskService(file);
    }

    private void loadOrNotify(Path filePath) {
        if (Files.exists(filePath)) {
            System.out.println("Файл найден, загружаем данные." + filePath);
            loadFromFile(filePath);
        } else {
            throw new TaskServiceNotFoundException("File not found: " + filePath);
        }
    }

    public void loadFromFile(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                System.out.println("Loaded task: " + task);

                if (task instanceof Epic) {
                    addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    addSubtask((Subtask) task);
                } else if (task != null) {
                    addTask(task);
                }
            }
        } catch (IOException e) {
            throw new TasksServiceSaveException("Can't read from a file: " + e.getMessage());
        }

    }

    public void save() {
        List<Object> allTasks = new ArrayList<>();
        allTasks.addAll(getTasks());
        allTasks.addAll(getEpics());
        allTasks.addAll(getSubtasks());

        try (var writer = Files.newBufferedWriter(filePath)) {
            for (Object task : allTasks) {
                writer.write(toString((Task) task));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new TasksServiceSaveException("Can't write to a file: " + e.getMessage());
        }

        if (allTasks.isEmpty()) {
            System.out.println("Файл пуст, записываем заголовок.");
            try (var writer = Files.newBufferedWriter(filePath)) {
                writer.write("id,type,name,status,description,duration,startTime,epicId");
                writer.newLine();
            } catch (IOException e) {
                throw new TasksServiceSaveException("Can't write to a file: " + e.getMessage());
            }
        }
    }

    public String toString(Task task) {
        TaskType type = task.getType();
        String typeToSave = type == TaskType.EPIC ? "epic" : type == TaskType.SUBTASK ? "subtask" : "task";
        String duration = task.getDuration() == null ? "none" : String.valueOf(task.getDuration().toMinutes());
        String startTime = task.getStartTime() == null ? "none" : task.getStartTime().toString();

        if (type == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;

            return subtask.getId() + "," +
                    typeToSave + "," +
                    subtask.getName() + "," +
                    subtask.getStatus() + "," +
                    subtask.getDescription() + "," +
                    duration + "," +
                    startTime + "," +
                    subtask.getParentTaskId();
        }

        return task.getId() + "," +
                typeToSave + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                duration + "," +
                startTime;
    }

    public Status convertToStatus(String status) {
        return Status.valueOf(status);
    }

    public Task fromString(String str) {
        // Skip the header
        if (str.startsWith("id")) {
            return null;
        }

        String[] parts = str.split(",");

        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = convertToStatus(parts[3]);
        String description = parts[4];
        String duration = parts[5];
        String startTime = parts[6];

        Duration durationFinal;
        LocalDateTime startTimeFinal;

        durationFinal = Objects.equals(duration, "none") ? null : Duration.ofMinutes(Long.parseLong(duration));
        startTimeFinal = Objects.equals(startTime, "none") ? null : LocalDateTime.parse(startTime);


        if (parts.length == 8) {
            int epicId = Integer.parseInt(parts[7]);
            Subtask subtask = durationFinal != null
                    ? new Subtask(name, description, status, epicId, durationFinal.toMinutes(), startTimeFinal)
                    : new Subtask(name, description, status, epicId);
            subtask.setId(id);
            return subtask;
        }

        if (type.equals("task")) {
            Task task = durationFinal != null
                    ? new Task(name, description, status, durationFinal.toMinutes(), startTimeFinal)
                    : new Task(name, description, status);
            task.setId(id);
            return task;
        } else {
            Epic epic = new Epic(name, description, status);
            epic.setId(id);
            return epic;
        }
    }


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(Task task) {
        super.removeTask(task);
        save();
    }

    @Override
    public void removeEpic(Epic epic) {
        super.removeEpic(epic);
        save();
    }

    @Override
    public void removeSubtask(Subtask subtask) {
        super.removeSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }
}
