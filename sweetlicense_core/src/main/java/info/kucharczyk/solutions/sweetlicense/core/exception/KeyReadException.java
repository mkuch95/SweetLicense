package info.kucharczyk.solutions.sweetlicense.core.exception;

public class KeyReadException extends SweetLicenseException {
    public KeyReadException(String message){
        super(message);
    }

    public KeyReadException(String message, Throwable cause){
        super(message, cause);
    }
}
