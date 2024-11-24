package api.tasks.model;

import api.tasks.adapters.TasksDurationAdapter;
import api.tasks.adapters.TasksLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskGson {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new TasksLocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new TasksDurationAdapter())
            .create();

}
