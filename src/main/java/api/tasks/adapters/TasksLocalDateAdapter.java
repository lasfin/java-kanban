package api.tasks.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TasksLocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void write(final JsonWriter out, final LocalDate localDate) throws IOException {
        out.value(localDate  != null ? localDate.format(dtf) : null);
    }

    @Override
    public LocalDate read(final JsonReader in) throws IOException {
        return LocalDate.parse(in.nextString(), dtf);
    }
}