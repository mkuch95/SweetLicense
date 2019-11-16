package info.kucharczyk.solutions.sweetlicense.demo.authority.model;

import info.kucharczyk.solutions.sweetlicense.core.model.BaseLicenseRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * License Request model
 */
public class LicenseRequest extends BaseLicenseRequest {
    /**
     * Client version
     */
    private String appVersion;
    /**
     * values identify the device
     */
    private String deviceFingerprint;
    /**
     * Modules available in the system
     */
    public List<String> availablePrinters;

    public LicenseRequest() {
        super();
        this.availablePrinters = new ArrayList<>();
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public List<String> getAvailablePrinters() {
        return availablePrinters;
    }
}
