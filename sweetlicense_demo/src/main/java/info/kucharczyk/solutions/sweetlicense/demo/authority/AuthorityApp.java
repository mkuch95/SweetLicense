package info.kucharczyk.solutions.sweetlicense.demo.authority;

import info.kucharczyk.solutions.sweetlicense.core.signer.LicenseRequestSigner;
import info.kucharczyk.solutions.sweetlicense.core.signer.LicenseSigner;
import info.kucharczyk.solutions.sweetlicense.demo.authority.model.License;
import info.kucharczyk.solutions.sweetlicense.demo.authority.model.LicenseRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class AuthorityApp {
    private static final String KEYSTORE_PATH = "authority/server.jks";
    private static final String KEYSTORE_PASSWORD = "main123";

    public static final String LICENSE_PATH = "authority/lic.txt";
    public static final String LICENSE_REQUEST_PATH = "authority/licReq.txt";

    private KeyStore getKeyStore() throws Exception{
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream is = new FileInputStream(new File(KEYSTORE_PATH))) {
            keyStore.load(is, KEYSTORE_PASSWORD.toCharArray());
        }
        return keyStore;
    }

    public static void main(String args[]) throws Exception {
        AuthorityApp app = new AuthorityApp();
        System.out.println("AuthorityApp started!");
        app.main();
        System.out.println("AuthorityApp closed!");
    }

    private void generateLicenseFromLicenseRequest(String licenseRequestPath, String licensePath) throws Exception {
        LicenseRequestSigner<LicenseRequest> licenseRequestSigner = new LicenseRequestSigner<>(LicenseRequest.class);
        LicenseSigner<License> licenseSigner = new LicenseSigner<>(License.class);

        KeyStore keyStore = getKeyStore();
        LicenseRequest licenseRequest;
        try (FileInputStream fis = new FileInputStream(licenseRequestPath)) {
            licenseRequest = licenseRequestSigner.readLicenseRequest(keyStore, fis);
        }
        System.out.println("License request for: " + licenseRequest.getDeviceFingerprint());
        String alias = licenseRequest.getTransportHeader().getEncryptionAlias();
        System.out.println("License request alias: " + alias);

        //create license based on license request
        License license = new License();
        license.getTransportHeader().setEncryptionAlias(alias);
        license.setDeviceFingerprint(licenseRequest.getDeviceFingerprint());
        license.getAllowedPrinters().addAll(licenseRequest.getAvailablePrinters());

        System.out.println("This device has " + licenseRequest.getAvailablePrinters().size() +
                " connected printers, the maximum allowed number of printers for this customer is 3...");
        //limit numbers of printers to 3
        while (license.getAllowedPrinters().size() > 3) {
            license.getAllowedPrinters().remove(0);
        }

        try (FileOutputStream fos = new FileOutputStream(licensePath)) {
            licenseSigner.writeLicense(keyStore, license, fos);
        }
        System.out.println("License for: " + licenseRequest.getDeviceFingerprint() + " created");
    }

    private void main() throws Exception {
        File licenseRequestFile = new File(LICENSE_REQUEST_PATH);

        if(!licenseRequestFile.exists()){
            System.out.println("Nothing to do...");
            return;
        }
        System.out.println("License request file found");

        generateLicenseFromLicenseRequest(LICENSE_REQUEST_PATH, LICENSE_PATH);

        licenseRequestFile.delete();
        System.out.println("License request file removed");
    }
}
