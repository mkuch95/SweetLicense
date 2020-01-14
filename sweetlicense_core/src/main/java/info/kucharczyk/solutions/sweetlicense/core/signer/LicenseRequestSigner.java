package info.kucharczyk.solutions.sweetlicense.core.signer;

import info.kucharczyk.solutions.sweetlicense.core.exception.EncryptorExtension;
import info.kucharczyk.solutions.sweetlicense.core.exception.InvalidAliasException;
import info.kucharczyk.solutions.sweetlicense.core.exception.KeyReadException;
import info.kucharczyk.solutions.sweetlicense.core.model.BaseLicenseRequest;
import info.kucharczyk.solutions.sweetlicense.core.model.TransportHeader;
import info.kucharczyk.solutions.sweetlicense.core.util.InputStreamUtils;

import java.io.*;
import java.security.*;

public class LicenseRequestSigner<LR extends BaseLicenseRequest> extends BaseSigner<LR> {
    public LicenseRequestSigner(Class<LR> licenseRequestClazz) {
        super(licenseRequestClazz);
    }

    public LR readLicenseRequest(KeyStore keyStore, InputStream is)
            throws IOException, KeyStoreException, EncryptorExtension, KeyReadException {
        String headerLine = InputStreamUtils.readLine(is);
        TransportHeader header = TransportHeader.fromString(headerLine);
        String alias = header.getEncryptionAlias();

        Key privateKey = null;
        try {
            privateKey = keyStore.getKey(alias, "".toCharArray());
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new KeyReadException("Cannot read key", e);
        }
        return read(privateKey, is);
    }

    public void writeLicenseRequest(KeyStore keyStore, LR licenseRequest, OutputStream os)
            throws IOException, KeyStoreException, InvalidAliasException, EncryptorExtension {
        TransportHeader header = licenseRequest.getTransportHeader();
        String alias = header.getEncryptionAlias();
        if(alias == null || !keyStore.containsAlias(alias)){
            throw new InvalidAliasException("Alias does not exist");
        }
        os.write(header.toString().getBytes());
        os.write('\n');

        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
        write(publicKey, licenseRequest, os);
    }
}
