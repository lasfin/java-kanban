package services.api;

import api.HttpTaskServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

import static com.google.gson.JsonParser.parseString;

public class HistoryHandlerTest {
    private HttpClient client;
    private final int port = 8081;
    private final TaskService taskService = Managers.getDefault();
    private final String baseUrl = "http://localhost:" + port + "/history";
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
    void shouldReturnHistoryOfViews() throws IOException, InterruptedException {
        HttpRequest requestTask1 = HttpRequest.newBuilder()
                .uri(URI.create(tasksUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Test task\",\"description\":\"Test description\"}"))
                .build();

        HttpRequest requestTask2 = HttpRequest.newBuilder()
                .uri(URI.create(tasksUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Test task2\",\"description\":\"Test description2\"}"))
                .build();

        client.send(requestTask1, HttpResponse.BodyHandlers.ofString());
        client.send(requestTask2, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create(baseUrl);

        HttpRequest view1 = HttpRequest.newBuilder()
                .uri(URI.create(tasksUrl + "/1"))
                .GET()
                .build();

        HttpRequest view2 = HttpRequest.newBuilder()
                .uri(URI.create(tasksUrl + "/2"))
                .GET()
                .build();

        client.send(view1, HttpResponse.BodyHandlers.ofString());
        client.send(view2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestHistory = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> responseList;

        responseList = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, responseList.statusCode());

        JsonElement jsonElement = parseString(responseList.body());

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Assertions.assertEquals(2, jsonArray.size());
    }
}
