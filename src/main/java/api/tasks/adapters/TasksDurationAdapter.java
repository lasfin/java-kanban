package api.tasks.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class TasksDurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter out, final Duration duration) throws IOException {
        out.value(duration != null ? duration.toString() : null);
    }

    @Override
    public Duration read(final JsonReader in) throws IOException {
        return Duration.parse(in.nextString());
    }
}
