package info.kucharczyk.solutions.sweetlicense.core.util;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamUtils {
    public static String readLine(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        int b;
        while((b = is.read()) != -1){
            if(b == '\r') break;
            if(b == '\n') break;
            sb.append((char) b);
        }
        return sb.toString();
    }
}
