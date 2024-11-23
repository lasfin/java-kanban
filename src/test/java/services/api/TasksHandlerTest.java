package services.api;

import api.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.Managers;
import services.task.TaskService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TasksHandlerTest {
    private HttpClient client;
    private final int port = 8081;
    private final TaskService taskService = Managers.getDefault();
    private final String baseUrl = "http://localhost:" + port + "/tasks";

    @BeforeEach
    void setUp() throws IOException {
        client = HttpClient.newHttpClient();
        HttpTaskServer.main(new String[]{}, taskService, port);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    void tearDown() {
        HttpTaskServer.stop();
    }

    @Test
    void shouldReturnEmptyList() throws IOException, InterruptedException {
        URI url = URI.create(baseUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response;

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.body(), "[]");
    }


    @Test
    void shouldCreateTask() throws IOException, InterruptedException {
        URI url = URI.create(baseUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Test task\",\"description\":\"Test description\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertTrue(response.body().contains("Test task"));
    }


}
