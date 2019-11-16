package info.kucharczyk.solutions.sweetlicense.demo.client.model;

import info.kucharczyk.solutions.sweetlicense.core.model.BaseLicense;

import java.util.ArrayList;
import java.util.List;

/**
 * License model
 */
public class License extends BaseLicense {
    /**
     * values identify the device
     */
    private String deviceFingerprint;
    /**
     * Modules allowed by the license
     */
    private List<String> allowedPrinters;

    public License(){
        super();
        this.allowedPrinters = new ArrayList<>();
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public List<String> getAllowedPrinters() {
        return allowedPrinters;
    }
}
