package api;

import api.tasks.TasksHandler;
import com.sun.net.httpserver.HttpServer;
import services.Managers;
import services.task.TaskService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args, TaskService taskService) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler(taskService));

        httpServer.start();
        System.out.println("Starting server on port " + PORT);
    }
}
