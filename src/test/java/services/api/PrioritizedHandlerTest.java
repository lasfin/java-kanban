package services.api;

import api.HttpTaskServer;
import api.tasks.model.TaskGson;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import model.Status;
import model.Task;
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
import java.time.LocalDateTime;

import static com.google.gson.JsonParser.parseString;

public class PrioritizedHandlerTest {
    private HttpClient client;
    private final int port = 8081;
    private final TaskService taskService = Managers.getDefault();
    private final Gson gson = TaskGson.GSON;

    private final String baseUrl = "http://localhost:" + port + "/prioritized";
    private final String tasksUrl = "http://localhost:" + port + "/tasks";

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
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("[]", response.body());
    }

    @Test
    void shouldReturnPrioritizedTasks() throws IOException, InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(1);

        Task task = new Task("Test task", "Test description", Status.NEW, 10, now);
        Task task2 = new Task("Test task2", "Test description2", Status.NEW, 20, later);

        String jsonBodyTask = gson.toJson(task);
        String jsonBodyTask2 = gson.toJson(task2);

        HttpRequest requestTask1 = HttpRequest.newBuilder()
                .uri(URI.create(tasksUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBodyTask))
                .build();

        HttpRequest requestTask2 = HttpRequest.newBuilder()
                .uri(URI.create(tasksUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBodyTask2))
                .build();

        client.send(requestTask1, HttpResponse.BodyHandlers.ofString());
        client.send(requestTask2, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create(baseUrl);

        HttpRequest requestHistory = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> responseListPrioritized;

        responseListPrioritized = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, responseListPrioritized.statusCode());

        JsonElement jsonElement = parseString(responseListPrioritized.body());

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        Assertions.assertEquals(2, jsonArray.size());
    }
}

