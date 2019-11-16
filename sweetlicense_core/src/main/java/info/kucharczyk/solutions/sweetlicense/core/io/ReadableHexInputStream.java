package info.kucharczyk.solutions.sweetlicense.core.io;

import java.io.IOException;
import java.io.InputStream;

public class ReadableHexInputStream extends InputStream {
    private final InputStream inputStream;

    public ReadableHexInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        String hex = "";

        while(hex.length() < 2){
            int inputByte = inputStream.read();
            if(inputByte < 0){
                return inputByte;
            }
            if(!Character.isLetterOrDigit(inputByte)){
                continue;
            }
            hex += (char)inputByte;
        }
        int lPart = Character.digit(hex.charAt(0), 16);
        int rPart = Character.digit(hex.charAt(1), 16);
        return lPart << 4 | rPart;
    }
}
