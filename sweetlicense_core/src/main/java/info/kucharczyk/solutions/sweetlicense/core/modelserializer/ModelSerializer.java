package info.kucharczyk.solutions.sweetlicense.core.modelserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class ModelSerializer<T> {
    public abstract void serialize(T model, OutputStream os) throws IOException;
    public abstract T deserialize(InputStream is) throws IOException;
}
