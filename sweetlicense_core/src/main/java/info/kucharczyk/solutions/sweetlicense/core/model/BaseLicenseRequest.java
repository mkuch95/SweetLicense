package info.kucharczyk.solutions.sweetlicense.core.model;

/**
 * License Request model
 */
public abstract class BaseLicenseRequest {
    private TransportHeader transportHeader;

    public BaseLicenseRequest() {
        this.transportHeader = new TransportHeader("LICENSE REQUEST");
    }

    public TransportHeader getTransportHeader() {
        return transportHeader;
    }

    public void setTransportHeader(TransportHeader transportHeader) {
        this.transportHeader = transportHeader;
    }
}
