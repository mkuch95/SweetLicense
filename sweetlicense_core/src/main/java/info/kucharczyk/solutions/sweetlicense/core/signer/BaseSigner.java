package info.kucharczyk.solutions.sweetlicense.core.signer;

import info.kucharczyk.solutions.sweetlicense.core.encryptor.Encryptor;
import info.kucharczyk.solutions.sweetlicense.core.encryptor.RsaAesEncryptor;
import info.kucharczyk.solutions.sweetlicense.core.exception.EncryptorExtension;
import info.kucharczyk.solutions.sweetlicense.core.io.ReadableHexInputStream;
import info.kucharczyk.solutions.sweetlicense.core.io.ReadableHexOutputStream;
import info.kucharczyk.solutions.sweetlicense.core.modelserializer.JsonModelSerializer;
import info.kucharczyk.solutions.sweetlicense.core.modelserializer.ModelSerializer;

import java.io.*;
import java.security.Key;

public abstract class BaseSigner<T> {
    private int pairsInLine = 40;
    private final Encryptor encryptor;
    private final ModelSerializer<T> modelSerializer;

    public BaseSigner(Class<T> clazz) {
        this.modelSerializer = new JsonModelSerializer<>(clazz);
        this.encryptor = new RsaAesEncryptor();
    }

    protected T read(Key key, InputStream is) throws IOException, EncryptorExtension {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (InputStream ris = new ReadableHexInputStream(is)) {
                encryptor.decrypt(key, ris, baos);
            }
            try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())) {
                return modelSerializer.deserialize(bais);
            }
        }
    }

    public void write(Key key, T model, OutputStream os) throws IOException, EncryptorExtension {
        try (OutputStream ros = new ReadableHexOutputStream(os, pairsInLine)) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                modelSerializer.serialize(model, baos);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())) {
                    encryptor.encrypt(key, bais, ros);
                }
            }
        }
    }
}
