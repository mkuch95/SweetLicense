package info.kucharczyk.solutions.sweetlicense.demo.client;

import info.kucharczyk.solutions.sweetlicense.core.signer.LicenseRequestSigner;
import info.kucharczyk.solutions.sweetlicense.core.signer.LicenseSigner;
import info.kucharczyk.solutions.sweetlicense.demo.client.model.License;
import info.kucharczyk.solutions.sweetlicense.demo.client.model.LicenseRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class ClientApp {
    // trustedStore location
    private static final String TRUSTEDSTORE_PATH = "client/trustedclient.jks";
    private static final String TRUSTEDSTORE_PASSWORD = "main123";

    public static final String LICENSE_PATH = "client/lic.txt";
    public static final String LICENSE_REQUEST_PATH = "client/licReq.txt";

    // mock client app info
    private static final String CLIENT_APP_VERSION = "1.2.3.4";
    private static final String MOTHERBOARD_SERIAL_NUMBER = "ABC123ABC123MMM";

    private List<String> getConnectedPrinters() {
        List<String> result = new ArrayList<>();
        result.add("ink_printer1");
        result.add("ink_printer2");
        result.add("laser_printer3");
        result.add("laser_printer4");
        return result;
    }

    private KeyStore getTrustedStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream is = new FileInputStream(TRUSTEDSTORE_PATH)) {
            keyStore.load(is, TRUSTEDSTORE_PASSWORD.toCharArray());
        }
        return keyStore;
    }

    public static void main(String args[]) throws Exception {
        ClientApp app = new ClientApp();
        System.out.println("ClientApp started!");
        app.main();
        System.out.println("ClientApp closed!");
    }

    private void createLicenseRequest() throws Exception {
        //simulate client
        LicenseRequest licenseRequest = new LicenseRequest();
        licenseRequest.setAppVersion(CLIENT_APP_VERSION);
        licenseRequest.setDeviceFingerprint(MOTHERBOARD_SERIAL_NUMBER);
        licenseRequest.getAvailablePrinters().addAll(getConnectedPrinters());

        LicenseRequestSigner<LicenseRequest> licenseRequestSigner = new LicenseRequestSigner<>(LicenseRequest.class);
        try (InputStream is = new FileInputStream(new File(TRUSTEDSTORE_PATH))) {
            KeyStore trustedStore = KeyStore.getInstance("JKS");
            trustedStore.load(is, TRUSTEDSTORE_PASSWORD.toCharArray());

            try (FileOutputStream fos = new FileOutputStream(LICENSE_REQUEST_PATH)) {
                licenseRequestSigner.writeLicenseRequest(trustedStore, licenseRequest, fos);
            }
        }
    }

    private List<String> allowedPrinters = new ArrayList<>();

    private boolean readLicense() throws Exception {
        File licenseFile = new File(LICENSE_PATH);

        if (!licenseFile.exists()) {
            System.out.println("There is no license file...");
            return false;
        }
        System.out.println("The license file found!");

        LicenseSigner<License> licenseSigner = new LicenseSigner<>(License.class);
        KeyStore trustedStore = getTrustedStore();
        License license;
        try (FileInputStream fis = new FileInputStream(licenseFile)) {
            license = licenseSigner.readLicense(trustedStore, fis);
        }

        if (!MOTHERBOARD_SERIAL_NUMBER.equals(license.getDeviceFingerprint())) {
            System.out.println("ClientApp: it's not my license!");
            return false;
        }

        // looks good...
        this.allowedPrinters.addAll(license.getAllowedPrinters());

        return true;
    }

    private void main() throws Exception {
        System.out.println("Connected printers:");
        System.out.println(" " + String.join(", ", getConnectedPrinters()));

        boolean licenseOk = readLicense();
        if (!licenseOk) {
            System.out.println("License check: error");
            createLicenseRequest();
            System.out.println("License request file created");
            return;
        }
        System.out.println("license check: ok");

        System.out.println("Printers allowed by the license:");
        System.out.println(" " + String.join(", ", this.allowedPrinters));
    }
}