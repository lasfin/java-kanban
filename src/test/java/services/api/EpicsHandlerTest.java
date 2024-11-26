package services.api;

import api.HttpTaskServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.*;
import services.Managers;
import services.task.TaskService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.google.gson.JsonParser.parseString;

public class EpicsHandlerTest {
    private HttpClient client;
    private final int port = 8081;
    private final TaskService taskService = Managers.getDefault();
    private final String baseUrl = "http://localhost:" + port + "/epics";

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
    void shouldCreateEpic() throws IOException, InterruptedException {
        URI url = URI.create(baseUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Test epic\",\"description\":\"Test description\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertTrue(response.body().contains("Test epic"));
    }

    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {
        URI url = URI.create(baseUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Test epic\",\"description\":\"Test description\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = parseString(response.body());

        url = URI.create(baseUrl + "/" + jsonElement.getAsJsonObject().get("id").getAsString());
        request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Updated epic\",\"description\":\"Updated description\"}"))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertTrue(response.body().contains("Updated epic"));
    }

    @Test
    void shouldDeleteEpic() throws IOException, InterruptedException {
        URI url = URI.create(baseUrl);
        HttpRequest requestCreate = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Test epic\",\"description\":\"Test description\"}"))
                .build();

        HttpResponse<String> response = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = parseString(response.body());

        url = URI.create(baseUrl + "/" + jsonElement.getAsJsonObject().get("id").getAsString());
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        URI urlGet2 = URI.create(baseUrl + "/" + jsonElement.getAsJsonObject().get("id").getAsString());

        HttpRequest requestGet2 = HttpRequest.newBuilder()
                .uri(urlGet2)
                .GET()
                .build();

        HttpResponse responseGet2 = client.send(requestGet2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, responseGet2.statusCode());
    }

    @Test
    void shouldReturnEpicsList() throws IOException, InterruptedException {
        HttpRequest requestTask1 = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Test epic\",\"description\":\"Test description\"}"))
                .build();

        HttpRequest requestTask2 = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Test epic2\",\"description\":\"Test description2\"}"))
                .build();

        client.send(requestTask1, HttpResponse.BodyHandlers.ofString());
        client.send(requestTask2, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create(baseUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> responseList;

        responseList = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, responseList.statusCode());
        JsonElement jsonElement = parseString(responseList.body());
        JsonArray jsonObject = jsonElement.getAsJsonArray();
        Assertions.assertEquals(2, jsonObject.size());
    }

}

