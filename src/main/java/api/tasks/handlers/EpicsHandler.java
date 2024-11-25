package api.tasks.handlers;

import api.tasks.model.TaskGson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Task;
import services.task.TaskService;

import java.io.IOException;
import java.util.Map;

public class EpicsHandler extends BaseHandler {
    private final TaskService tasks;

    public EpicsHandler(TaskService taskService) {
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
                Epic task = tasks.getEpic(Integer.parseInt(pathParts[2]));
                sendResponse(exchange, task, 200);
            } catch (Exception e) {
                sendResponse(exchange, Map.of("error", "Task not found"), 404);
            }
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Epic newEpic = TaskGson.GSON.fromJson(body, Epic.class);

        boolean epicExists;
        try {
            epicExists = tasks.getEpic(newEpic.getId()) != null;
        } catch (Exception e) {
            epicExists = false;
        }

        if (!epicExists) {
            sendResponse(exchange, tasks.addEpic(newEpic), 201);
        } else {
            Task existingTask = tasks.getTask(newEpic.getId());

            if (existingTask != null) {
                updateTask(newEpic);
                sendResponse(exchange, newEpic, 201);
            } else {
                sendResponse(exchange, Map.of("error", "Task not found"), 404);
            }
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");
        if (pathParts.length == 3) {
            Epic epic = tasks.getEpic(Integer.parseInt(pathParts[2]));
            if (epic != null) {
                tasks.removeEpic(epic);
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