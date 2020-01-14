package info.kucharczyk.solutions.sweetlicense.core.signer;

import info.kucharczyk.solutions.sweetlicense.core.exception.EncryptorExtension;
import info.kucharczyk.solutions.sweetlicense.core.exception.InvalidAliasException;
import info.kucharczyk.solutions.sweetlicense.core.exception.KeyReadException;
import info.kucharczyk.solutions.sweetlicense.core.model.BaseLicense;
import info.kucharczyk.solutions.sweetlicense.core.model.TransportHeader;
import info.kucharczyk.solutions.sweetlicense.core.util.InputStreamUtils;

import java.io.*;
import java.security.*;

public class LicenseSigner<L extends BaseLicense> extends BaseSigner<L> {
    public LicenseSigner(Class<L> licenseClazz) {
        super(licenseClazz);
    }

    public L readLicense(KeyStore keyStore, InputStream is)
            throws IOException, KeyStoreException, EncryptorExtension {
        String headerLine = InputStreamUtils.readLine(is);
        TransportHeader header = TransportHeader.fromString(headerLine);
        String alias = header.getEncryptionAlias();

        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
        return read(publicKey, is);
    }

    public void writeLicense(KeyStore keyStore, L license, OutputStream os)
            throws IOException, KeyStoreException, InvalidAliasException, EncryptorExtension, KeyReadException {
        TransportHeader header = license.getTransportHeader();
        String alias = header.getEncryptionAlias();
        if(alias == null) {
            throw new InvalidAliasException("alias must be set in the header!");
        }
        os.write(header.toString().getBytes());
        os.write('\n');

        Key privateKey = null;
        try {
            privateKey = keyStore.getKey(alias, "".toCharArray());
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new KeyReadException("Cannot read key", e);
        }
        write(privateKey, license, os);
    }
}
