package services.task;

import model.*;
import services.exeptions.TasksServiceSaveException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


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
            System.out.println("Файл не найден");
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
                writer.write("id,type,name,status,description,epicId,duration,startTime");
                writer.newLine();
            } catch (IOException e) {
                throw new TasksServiceSaveException("Can't write to a file: " + e.getMessage());
            }
        }
    }

    public String toString(Task task) {
        TaskType type = task.getType();
        String typeToSave = type == TaskType.EPIC ? "epic" : type == TaskType.SUBTASK ? "subtask" : "task";

        if (type == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + typeToSave + "," + subtask.getName() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getParentTaskId();
        }

        return task.getId() + "," + typeToSave + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
    }

    public Status convertToStatus(String status) {
        if (status.equals("NEW")) {
            return Status.NEW;
        } else if (status.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }

    public Task fromString(String str) {
        if (str.startsWith("id")) {
            return null;
        }

        String[] parts = str.split(",");

        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = convertToStatus(parts[3]);
        String description = parts[4];

        if (parts.length == 6) {
            int epicId = Integer.parseInt(parts[5]);
            Subtask subtask = new Subtask(name, description, status, epicId);
            subtask.setId(id);
            return subtask;
        }

        if (type.equals("task")) {
            Task task = new Task(name, description, status);
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
