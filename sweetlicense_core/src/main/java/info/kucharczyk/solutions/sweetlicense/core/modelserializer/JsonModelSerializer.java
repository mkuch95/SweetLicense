package info.kucharczyk.solutions.sweetlicense.core.modelserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonModelSerializer<T> extends ModelSerializer<T> {
    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    public JsonModelSerializer(Class<T> clazz){
        this.clazz = clazz;
        objectMapper = new ObjectMapper();
        objectMapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public void serialize(T model, OutputStream os) throws IOException {
        objectMapper.writeValue(os, model);
    }

    public T deserialize(InputStream is) throws IOException {
        return objectMapper.readValue(is, this.clazz);
    }
}
