package api.tasks.handlers;

import api.tasks.adapters.TasksDurationAdapter;
import api.tasks.adapters.TasksLocalDateAdapter;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import model.Status;
import model.Task;
import services.task.TaskService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class TasksHandler extends BaseHandler {
    private final TaskService tasks;

    public TasksHandler(TaskService taskService) {
        super(new GsonBuilder()
                .registerTypeAdapter(Duration.class, new TasksDurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new TasksLocalDateAdapter())
                .setPrettyPrinting()
                .create());

        this.tasks = taskService;
    }

    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");
        if (pathParts.length == 2) {
            sendResponse(exchange, tasks.getTasks(), 200);
        } else if (pathParts.length == 3) {
            try {
                Task task = tasks.getTask(Integer.parseInt(pathParts[2]));
                sendResponse(exchange, task, 200);
            } catch (Exception e) {
                sendResponse(exchange, Map.of("error", "Task not found"), 404);
            }
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        var json = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Map.class);

        if (json.get("id") == null) {
            Task newTask = new Task(
                    (String) json.get("name"),
                    (String) json.get("description"),
                    Status.NEW
            );
            sendResponse(exchange, tasks.addTask(newTask), 201);
        } else {
            Task existingTask = tasks.getTask(Integer.parseInt((String) json.get("id")));
            if (existingTask != null) {
                updateTask(existingTask, json);
                sendResponse(exchange, existingTask, 201);
            } else {
                sendResponse(exchange, Map.of("error", "Task not found"), 404);
            }
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");
        if (pathParts.length == 3) {
            Task task = tasks.getTask(Integer.parseInt(pathParts[2]));
            if (task != null) {
                tasks.removeTask(task);
                sendResponse(exchange, null, 200);
            } else {
                sendResponse(exchange, Map.of("error", "Task not found"), 404);
            }
        }
    }

    private void updateTask(Task task, Map<String, Object> json) {
        if (json.get("name") != null) task.setName((String) json.get("name"));
        if (json.get("description") != null) task.setDescription((String) json.get("description"));
        if (json.get("status") != null) task.setStatus(Status.valueOf((String) json.get("status")));
        if (json.get("duration") != null) task.setDuration(Duration.parse((String) json.get("duration")));
    }
}