package api.tasks.handlers;

import api.tasks.adapters.TasksDurationAdapter;
import api.tasks.adapters.TasksLocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Status;
import model.Task;
import services.task.TaskService;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Logger LOGGER = Logger.getLogger(TasksHandler.class.getName());
    private final TaskService tasks;
    private final Gson gson;

    public TasksHandler(TaskService taskService) {
        this.tasks = taskService;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Duration.class, new TasksDurationAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new TasksLocalDateAdapter());

        this.gson = gsonBuilder
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            JsonElement jsonElementBody = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), JsonElement.class);

            LOGGER.info("Handling request: " + method + " " + path);
            Endpoint endpoint = getEndpoint(path, method);

            switch (endpoint) {
                case GET_TASKS -> {
                    try {
                        var tasks = this.tasks.getTasks();
                        writeResponse(exchange, tasks, 200);
                    } catch (Exception e) {
                        LOGGER.severe("Error getting tasks: " + e.getMessage());
                        writeResponse(exchange, Map.of("error", "Error retrieving tasks"), 500);
                    }
                }
                case GET_TASK -> {
                    try {
                        String[] pathParts = path.split("/");
                        String taskId = pathParts[2];
                        var task = this.tasks.getTask(Integer.parseInt(taskId));
                        if (task == null) {
                            writeResponse(exchange, Map.of("error", "Task not found"), 404);
                        } else {
                            writeResponse(exchange, task, 200);
                        }
                    } catch (Exception e) {
                        LOGGER.severe("Error getting task: " + e.getMessage());
                        writeResponse(exchange, Map.of("error", "Error retrieving task"), 500);
                    }
                }
                case DELETE_TASK -> {
                    try {
                        String[] pathParts = path.split("/");
                        String taskId = pathParts[2];
                        Task taskToDelete = this.tasks.getTask(Integer.parseInt(taskId));

                        this.tasks.removeTask(taskToDelete);
                        writeResponse(exchange, null, 201);
                    } catch (Exception e) {
                        LOGGER.severe("Error deleting task: " + e.getMessage());
                        writeResponse(exchange, Map.of("error", "Error deleting task"), 500);
                    }
                }
                case UPDATE_OR_CREATE_TASK -> {
                    try {
                        JsonElement id = jsonElementBody
                                .getAsJsonObject()
                                .get("id");

                        if (id == null) {
                            String name = jsonElementBody.getAsJsonObject()
                                    .get("name").getAsString();
                            String description = jsonElementBody.getAsJsonObject()
                                    .get("description").getAsString();

                            Task added = this.tasks.addTask(new Task(
                                    name,
                                    description,
                                    Status.NEW
                            ));

                            writeResponse(exchange, added, 200);
                        } else {
                            try (InputStream is = exchange.getRequestBody();
                                 InputStreamReader isr = new InputStreamReader(is);
                                 BufferedReader br = new BufferedReader(isr)) {
                                Task taskToUpdate = this.tasks.getTask(Integer.parseInt(id.getAsString()));
                                String body = br.readLine();

                                Task updatedTask = gson.fromJson(body, Task.class);

                                taskToUpdate.setName(updatedTask.getName());
                                taskToUpdate.setDescription(updatedTask.getDescription());
                                taskToUpdate.setDuration(updatedTask.getDuration());
                                taskToUpdate.setStatus(updatedTask.getStatus());
                            }

                            writeResponse(exchange, null, 200);
                        }
                    } catch (Exception e) {
                        LOGGER.severe("Error updating task: " + e.getMessage());
                        writeResponse(exchange, Map.of("error", "Error updating task"), 500);
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
        } else if (pathParts.get(0).equals("tasks") && requestMethod.equals("GET") && pathParts.size() == 2) {
            return Endpoint.GET_TASK;
        } else if (pathParts.get(0).equals("tasks") && requestMethod.equals("POST")) {
            return Endpoint.UPDATE_OR_CREATE_TASK;
        } else if (pathParts.get(0).equals("tasks") && requestMethod.equals("DELETE") && pathParts.size() == 2) {
            return Endpoint.DELETE_TASK;
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

    enum Endpoint {
        GET_TASKS,
        GET_TASK,
        UPDATE_OR_CREATE_TASK,
        DELETE_TASK,
        UNKNOWN
    }
}


