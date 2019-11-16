package info.kucharczyk.solutions.sweetlicense.core.encryptor;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.util.Random;

public class RsaAesEncryptor extends Encryptor {
    private final static Random random = new Random();

    public RsaAesEncryptor() {
    }

    public void encrypt(Key key, InputStream is, OutputStream os) throws Exception {
        //generate AES private key and iv
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey aesKeySpec = keyGenerator.generateKey();
        byte[] iv = new byte[128 / 8];
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        //encrypt AES key by RSA
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = rsaCipher.doFinal(aesKeySpec.getEncoded());
        //write encrypted AES key and iv
        os.write(b);
        os.write(iv);

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, ivSpec);
        InputStream cis = new CipherInputStream(is, aesCipher);
        copyStream(cis, os);
    }

    public void decrypt(Key key, InputStream is, OutputStream os) throws Exception {
        //read encrypted AES key
        byte[] encryptedAesKey = new byte[256];
        is.read(encryptedAesKey);

        //use RSA to decrypt AES key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] aesKey = rsaCipher.doFinal(encryptedAesKey);
        SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, "AES");

        //read AES iv
        byte[] iv = new byte[128 / 8];
        is.read(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        //use AES to decrypt rest of the data
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKeySpec, ivSpec);

        InputStream cis = new CipherInputStream(is, aesCipher);
        copyStream(cis, os);
    }

    private void copyStream(InputStream in, OutputStream os) throws IOException {
        byte[] b = new byte[1024];
        int numberOfBytedRead;
        while ((numberOfBytedRead = in.read(b)) >= 0) {
            os.write(b, 0, numberOfBytedRead);
        }
    }
}
