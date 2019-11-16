package info.kucharczyk.solutions.sweetlicense.core.signer;


import info.kucharczyk.solutions.sweetlicense.core.model.BaseLicense;
import info.kucharczyk.solutions.sweetlicense.core.model.TransportHeader;
import info.kucharczyk.solutions.sweetlicense.core.util.InputStreamUtils;

import java.io.*;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;

public class LicenseSigner<L extends BaseLicense> extends BaseSigner<L> {
    public LicenseSigner(Class<L> licenseClazz) {
        super(licenseClazz);
    }

    public L readLicense(KeyStore keyStore, InputStream is) throws Exception {
        String headerLine = InputStreamUtils.readLine(is);
        TransportHeader header = TransportHeader.fromString(headerLine);
        String alias = header.getEncryptionAlias();

        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
        return read(publicKey, is);
    }

    public void writeLicense(KeyStore keyStore, L license, OutputStream os) throws Exception {
        TransportHeader header = license.getTransportHeader();
        String alias = header.getEncryptionAlias();
        if(alias == null) {
            throw new Exception("alias must be set!");
        }
        os.write(header.toString().getBytes());
        os.write('\n');

        Key privateKey = keyStore.getKey(alias, "".toCharArray());
        write(privateKey, license, os);
    }
}
