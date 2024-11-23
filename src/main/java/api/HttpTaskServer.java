package api;

import api.tasks.handlers.TasksHandler;
import com.sun.net.httpserver.HttpServer;
import services.task.TaskService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer httpServer;

    public static void main(String[] args, TaskService taskService, int port) throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(port), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskService));
        // httpServer.createContext("/history", new HistotyHandler(taskService));
        // httpServer.createContext("/prioritized", new PriotizedHandler(taskService));
        httpServer.start();
        System.out.println("Starting server on port " + port);
    }

    public static void stop() {
        httpServer.stop(0);
    }
}
