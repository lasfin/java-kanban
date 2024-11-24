package api.tasks.handlers;

import api.tasks.model.TaskGson;
import com.sun.net.httpserver.HttpExchange;
import services.task.TaskService;

import java.io.IOException;
import java.util.Map;

public class PrioritizedHandler extends BaseHandler {
    private final TaskService tasks;

    public PrioritizedHandler(TaskService taskService) {
        super(TaskGson.GSON);

        this.tasks = taskService;
    }

    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");


        if (pathParts.length == 2) {
            sendResponse(exchange, tasks.getPrioritizedTasks(), 200);
        } else {
            sendResponse(exchange, Map.of("error", "Not possible"), 400);
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        sendResponse(exchange, Map.of("error", "Not possible"), 400);
    }

    @Override
    protected void handleDelete(HttpExchange exchange, String path) throws IOException {
        sendResponse(exchange, Map.of("error", "Not possible"), 400);
    }
}
