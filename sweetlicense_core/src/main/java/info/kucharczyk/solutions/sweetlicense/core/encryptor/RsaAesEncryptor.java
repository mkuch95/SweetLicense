package info.kucharczyk.solutions.sweetlicense.core.encryptor;

import info.kucharczyk.solutions.sweetlicense.core.exception.EncryptorExtension;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Random;

public class RsaAesEncryptor extends Encryptor {
    private final static Random random = new Random();

    public RsaAesEncryptor() {
    }

    private byte[] encryptAesKey(Key rsaKey, Key aesKey)
            throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,
            IllegalBlockSizeException {
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, rsaKey);
        byte[] b = rsaCipher.doFinal(aesKey.getEncoded());
        //write encrypted AES key and iv
        return b;
    }

    private byte[] decryptAesKey(Key rsaKey, byte[] encryptedAesKey)
            throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,
            IllegalBlockSizeException {
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.DECRYPT_MODE, rsaKey);
        return rsaCipher.doFinal(encryptedAesKey);
    }

    public void encrypt(Key key, InputStream is, OutputStream os)
            throws IOException, EncryptorExtension {
        //generate AES private key and iv
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptorExtension("Cannot find algorithm", e);
        }
        keyGenerator.init(128);
        SecretKey aesKeySpec = keyGenerator.generateKey();
        byte[] iv = new byte[128 / 8];
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        //encrypt AES key by RSA
        byte[] b;
        try {
            b = encryptAesKey(key, aesKeySpec);
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException e) {
            throw new EncryptorExtension("Cannot encrypt coding data", e);
        }
        //write encrypted AES key and iv
        os.write(b);
        os.write(iv);

        Cipher aesCipher = null;
        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new EncryptorExtension("Cannot create Cipher", e);
        }
        try {
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, ivSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new EncryptorExtension("Cannot initialize AES Cipher", e);
        }
        InputStream cis = new CipherInputStream(is, aesCipher);
        copyStream(cis, os);
    }

    public void decrypt(Key key, InputStream is, OutputStream os)
            throws IOException, EncryptorExtension {
        //read encrypted AES key
        byte[] encryptedAesKey = new byte[256];
        is.read(encryptedAesKey);

        //use RSA to decrypt AES key
        byte[] aesKey;
        try{
            aesKey = decryptAesKey(key, encryptedAesKey);
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException e) {
            throw new EncryptorExtension("Cannot decrypt coding data", e);
        }
        SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, "AES");

        //read AES iv
        byte[] iv = new byte[128 / 8];
        is.read(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        //use AES to decrypt rest of the data
        Cipher aesCipher = null;
        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new EncryptorExtension("Cannot create Cipher", e);
        }
        try {
            aesCipher.init(Cipher.DECRYPT_MODE, aesKeySpec, ivSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new EncryptorExtension("Cannot initialize AES Cipher", e);
        }

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
