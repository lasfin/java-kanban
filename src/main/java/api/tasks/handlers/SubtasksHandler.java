package api.tasks.handlers;

import api.tasks.model.TaskGson;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import model.Task;
import services.task.TaskService;

import java.io.IOException;
import java.util.Map;

public class SubtasksHandler extends BaseHandler {
    private final TaskService tasks;

    public SubtasksHandler(TaskService taskService) {
        super(TaskGson.GSON);
        this.tasks = taskService;
    }

    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            sendResponse(exchange, tasks.getSubtasks(), 200);
        } else if (pathParts.length == 3) {
            try {
                Subtask st = tasks.getSubtask(Integer.parseInt(pathParts[2]));
                if (st == null) {
                    sendResponse(exchange, Map.of("error", "Subtask not found"), 404);
                } else {
                    sendResponse(exchange, st, 200);
                }
            } catch (Exception e) {
                sendResponse(exchange, Map.of("error", "Subtask not found"), 404);
            }
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Subtask newSubtask = TaskGson.GSON.fromJson(body, Subtask.class);

        boolean subtaskExists;
        try {
            subtaskExists = tasks.getSubtask(newSubtask.getId()) != null;
        } catch (Exception e) {
            subtaskExists = false;
        }

        if (!subtaskExists) {
            sendResponse(exchange, tasks.addSubtask(newSubtask), 201);
        } else {
            Subtask existingSubtask = tasks.getSubtask(newSubtask.getId());

            if (existingSubtask != null) {
                updateTask(newSubtask);
                sendResponse(exchange, newSubtask, 201);
            } else {
                sendResponse(exchange, Map.of("error", "Task not found"), 404);
            }
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");
        if (pathParts.length == 3) {
            boolean subtaskExists;
            try {
                subtaskExists = tasks.getSubtask(Integer.parseInt(pathParts[2])) != null;
            } catch (Exception e) {
                subtaskExists = false;
            }
            Subtask subtask = tasks.getSubtask(Integer.parseInt(pathParts[2]));

            if (subtaskExists) {
                tasks.removeSubtask(subtask);
                sendResponse(exchange, null, 200);
            } else {
                sendResponse(exchange, Map.of("error", "Epic not found"), 404);
            }
        }
    }

    private void updateTask(Task newTask) {
        tasks.updateTask(newTask);
    }
}