package api;

import api.tasks.handlers.TasksHandler;
import com.sun.net.httpserver.HttpServer;
import services.task.TaskService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static void main(String[] args, TaskService taskService, int port) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(port), 0);

        httpServer.createContext("/tasks", new TasksHandler(taskService));

        httpServer.start();
        System.out.println("Starting server on port " + port);
    }
}
