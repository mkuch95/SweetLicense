package info.kucharczyk.solutions.sweetlicense.core.io;

import java.io.IOException;
import java.io.OutputStream;

public class ReadableHexOutputStream extends OutputStream {
    private final OutputStream outputStream;
    private final int pairsInLineLimit;
    private String lineSeparator = "\n";
    private int pairsInLine;


    public ReadableHexOutputStream(OutputStream outputStream, int pairsInLineLimit) {
        if(pairsInLineLimit < 1){
            throw new IllegalArgumentException("pairsInLineLimit should be >= 1");
        }
        this.outputStream = outputStream;
        this.pairsInLineLimit = pairsInLineLimit;
    }

    @Override
    public void write(int b) throws IOException {
        int ubyte = Byte.toUnsignedInt((byte)b);
        String hex = String.format("%02x", ubyte).toUpperCase();
        outputStream.write(hex.getBytes());
        pairsInLine++;
        if(pairsInLine >= pairsInLineLimit){
            outputStream.write(lineSeparator.getBytes());
            pairsInLine = 0;
        }
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }
}
