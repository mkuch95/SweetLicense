package info.kucharczyk.solutions.sweetlicense.core.model;

/**
 * License model
 */
public abstract class BaseLicense {
    private TransportHeader transportHeader;

    public BaseLicense() {
        this.transportHeader = new TransportHeader("LICENSE");
    }

    public TransportHeader getTransportHeader() {
        return transportHeader;
    }

    public void setTransportHeader(TransportHeader transportHeader) {
        this.transportHeader = transportHeader;
    }
}
