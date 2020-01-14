package info.kucharczyk.solutions.sweetlicense.core.exception;

public class SweetLicenseException extends Exception {
    public SweetLicenseException(){
    }

    public SweetLicenseException(String message){
        super(message);
    }

    public SweetLicenseException(String message, Throwable cause){
        super(message, cause);
    }
}
