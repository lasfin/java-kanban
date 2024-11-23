package api.tasks.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

public abstract class BaseHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(BaseHandler.class.getName());
    protected final Gson gson;

    protected BaseHandler(Gson gson) {
        this.gson = gson;
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

    protected void sendResponse(HttpExchange exchange, Object response, int statusCode) throws IOException {
        String responseBody = response != null ? gson.toJson(response) : "";
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    protected abstract void handleGet(HttpExchange exchange, String path) throws IOException;
    protected abstract void handlePost(HttpExchange exchange) throws IOException;
    protected abstract void handleDelete(HttpExchange exchange, String path) throws IOException;
}