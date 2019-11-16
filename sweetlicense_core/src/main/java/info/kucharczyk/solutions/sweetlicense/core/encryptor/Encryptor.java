package info.kucharczyk.solutions.sweetlicense.core.encryptor;

import java.io.*;
import java.security.Key;

public abstract class Encryptor {
    public abstract void encrypt(Key key, InputStream is, OutputStream os) throws Exception; //TODO change Exception to something else
    public abstract void decrypt(Key key, InputStream is, OutputStream os) throws Exception; //TODO change Exception to something else
}
