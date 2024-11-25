package api.tasks.handlers;

import api.tasks.model.TaskGson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import services.task.TaskService;

import java.io.IOException;
import java.util.Map;

public class TasksHandler extends BaseHandler {
    private final TaskService tasks;

    public TasksHandler(TaskService taskService) {
        super(TaskGson.GSON);

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
        String body = readBody(exchange);
        Task newTask = TaskGson.GSON.fromJson(body, Task.class);

        boolean taskExists;
        try {
            taskExists = tasks.getTask(newTask.getId()) != null;
        } catch (Exception e) {
            taskExists = false;
        }

        if (!taskExists) {
            sendResponse(exchange, tasks.addTask(newTask), 201);
        } else {
            Task existingTask = tasks.getTask(newTask.getId());

            if (existingTask != null) {
                updateTask(newTask);
                sendResponse(exchange, newTask, 201);
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

    private void updateTask(Task newTask) {
        tasks.updateTask(newTask);
    }
}