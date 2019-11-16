package info.kucharczyk.solutions.sweetlicense.core.signer;


import info.kucharczyk.solutions.sweetlicense.core.model.BaseLicenseRequest;
import info.kucharczyk.solutions.sweetlicense.core.model.TransportHeader;
import info.kucharczyk.solutions.sweetlicense.core.util.InputStreamUtils;

import java.io.*;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;

public class LicenseRequestSigner<LR extends BaseLicenseRequest> extends BaseSigner<LR> {
    public LicenseRequestSigner(Class<LR> licenseRequestClazz) {
        super(licenseRequestClazz);
    }

    public LR readLicenseRequest(KeyStore keyStore, InputStream is) throws Exception {
        String headerLine = InputStreamUtils.readLine(is);
        TransportHeader header = TransportHeader.fromString(headerLine);
        String alias = header.getEncryptionAlias();

        Key privateKey = keyStore.getKey(alias, "".toCharArray());
        return read(privateKey, is);
    }

    public void writeLicenseRequest(KeyStore keyStore, LR licenseRequest, OutputStream os) throws Exception {
        TransportHeader header = licenseRequest.getTransportHeader();
        String alias = header.getEncryptionAlias();
        if(alias == null) {
            alias = keyStore.aliases().nextElement();
            header.setEncryptionAlias(alias);//get any
        }
        os.write(header.toString().getBytes());
        os.write('\n');

        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
        write(publicKey, licenseRequest, os);
    }
}
