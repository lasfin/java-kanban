package api.tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import services.task.TaskService;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Logger LOGGER = Logger.getLogger(TasksHandler.class.getName());
    private final TaskService tasks;
    private final Gson gson;

    public TasksHandler(TaskService taskService) {
        if (taskService == null) {
            throw new IllegalArgumentException("TaskService cannot be null");
        }
        this.tasks = taskService;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Task.class, new TasksDurationAdapter());
        gsonBuilder.registerTypeAdapter(Task.class, new TasksLocalDateAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new TasksLocalDateAdapter());

        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            LOGGER.info("Handling request: " + method + " " + path);

            Endpoint endpoint = getEndpoint(path, method);

            switch (endpoint) {
                case GET_TASKS -> {
                    try {
                        var tasks = this.tasks.getTasks();
                        if (tasks == null || tasks.isEmpty()) {
                            writeResponse(exchange, Map.of("error", "No tasks found"), 404);
                        } else {
                            writeResponse(exchange, tasks, 200);
                        }
                    } catch (Exception e) {
                        LOGGER.severe("Error getting tasks: " + e.getMessage());
                        writeResponse(exchange, Map.of("error", "Error retrieving tasks"), 500);
                    }
                }
                case GET_TASK -> {
                    try {
                        String[] pathParts = path.split("/");
                        if (pathParts.length < 2) {
                            writeResponse(exchange, Map.of("error", "Invalid task ID"), 400);
                            return;
                        }
                        String taskId = pathParts[2];
                        writeResponse(exchange, Map.of("taskId", taskId), 200);
                    } catch (Exception e) {
                        LOGGER.severe("Error getting task: " + e.getMessage());
                        writeResponse(exchange, Map.of("error", "Error retrieving task"), 500);
                    }
                }
                default -> writeResponse(exchange, Map.of("error", "Endpoint not found"), 404);
            }
        } catch (Exception e) {
            LOGGER.severe("Unhandled error in handler: " + e.getMessage());
            writeResponse(exchange, Map.of("error", "Internal server error"), 500);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        ArrayList<String> pathParts = new ArrayList<>();
        for (String part : requestPath.split("/")) {
            if (!part.isEmpty()) {
                pathParts.add(part);
            }
        }
        System.out.println("pathParts: " + pathParts);

        if (requestPath.equals("/tasks") && requestMethod.equals("GET")) {
            return Endpoint.GET_TASKS;
        } else if (pathParts.get(0).equals("tasks") && requestMethod.equals("POST")) {
            return Endpoint.GET_TASK;
        } else {
            return Endpoint.UNKNOWN;
        }
    }

    private void writeResponse(HttpExchange exchange, Object response, int responseCode) throws IOException {
        if (exchange == null) {
            throw new IllegalArgumentException("Exchange cannot be null");
        }

        String responseString = gson.toJson(response);
        byte[] responseBytes = responseString.getBytes(DEFAULT_CHARSET);

        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");

        exchange.sendResponseHeaders(responseCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
            os.flush();
        }

        exchange.close();
    }

    enum Endpoint {GET_TASKS, GET_TASK, UNKNOWN}
}


