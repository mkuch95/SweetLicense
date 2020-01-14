package info.kucharczyk.solutions.sweetlicense.core.encryptor;

import info.kucharczyk.solutions.sweetlicense.core.exception.EncryptorExtension;

import java.io.*;
import java.security.Key;

public abstract class Encryptor {
    public abstract void encrypt(Key key, InputStream is, OutputStream os) throws IOException, EncryptorExtension;
    public abstract void decrypt(Key key, InputStream is, OutputStream os) throws IOException, EncryptorExtension;
}
