package info.kucharczyk.solutions.sweetlicense.core.exception;

public class EncryptorExtension extends SweetLicenseException {
    public EncryptorExtension(String message){
        super(message);
    }

    public EncryptorExtension(String message, Throwable cause){
        super(message, cause);
    }
}
