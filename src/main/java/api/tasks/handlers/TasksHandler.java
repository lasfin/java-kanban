package api.tasks.handlers;

import api.tasks.adapters.TasksDurationAdapter;
import api.tasks.adapters.TasksLocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Status;
import model.Task;
import services.task.TaskService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Logger;

public class TasksHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(TasksHandler.class.getName());
    private final TaskService tasks;
    private final Gson gson;

    public TasksHandler(TaskService taskService) {
        this.tasks = taskService;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new TasksDurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new TasksLocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET" -> handleGet(exchange, path);
                case "POST" -> handlePost(exchange);
                case "DELETE" -> handleDelete(exchange, path);

                default -> sendResponse(exchange, Map.of("error", "Method not allowed"), 405);
            }
        } catch (Exception e) {
            LOGGER.severe("Error handling request: " + e.getMessage());
            sendResponse(exchange, Map.of("error", e.getMessage()), 500);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");
        if (pathParts.length == 2) {
            sendResponse(exchange, tasks.getTasks(), 200);
        } else if (pathParts.length == 3) {
            Task task = tasks.getTask(Integer.parseInt(pathParts[2]));
            if (task != null) {
                sendResponse(exchange, task, 200);
            } else {
                sendResponse(exchange, Map.of("error", "Task not found"), 404);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        var json = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Map.class);

        if (json.get("id") == null) {
            Task newTask = new Task(
                    (String) json.get("name"),
                    (String) json.get("description"),
                    Status.NEW
            );
            sendResponse(exchange, tasks.addTask(newTask), 200);
        } else {
            Task existingTask = tasks.getTask(Integer.parseInt((String) json.get("id")));
            if (existingTask != null) {
                updateTask(existingTask, json);
                sendResponse(exchange, existingTask, 200);
            } else {
                sendResponse(exchange, Map.of("error", "Task not found"), 404);
            }
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");
        if (pathParts.length == 3) {
            Task task = tasks.getTask(Integer.parseInt(pathParts[2]));
            if (task != null) {
                tasks.removeTask(task);
                sendResponse(exchange, null, 204);
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

    private void sendResponse(HttpExchange exchange, Object response, int statusCode) throws IOException {
        String responseBody = response != null ? gson.toJson(response) : "";
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}