package info.kucharczyk.solutions.sweetlicense.core.model;

import java.util.Objects;
import java.util.regex.Pattern;

public class TransportHeader {
    private static final String EDGE_TEXT = "-----";
    private static final String SEPARATOR_TEXT = "||";

    private String name;
    private String encryptionAlias;
    private String extraInfo;

    public TransportHeader() {
    }

    public TransportHeader(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String result = EDGE_TEXT;
        result += name + SEPARATOR_TEXT + encryptionAlias;
        if (this.extraInfo != null) {
            result += SEPARATOR_TEXT + extraInfo;
        }
        result += EDGE_TEXT;
        return result;
    }

    public static TransportHeader fromString(String value) {
        value = value.replaceAll("^" + Pattern.quote(EDGE_TEXT), "");
        value = value.replaceAll(Pattern.quote(EDGE_TEXT) + "$", "");
        String[] values = value.split(Pattern.quote(SEPARATOR_TEXT));

        TransportHeader result = new TransportHeader();
        result.setName(values[0]);
        result.setEncryptionAlias(values[1]);
        if (values.length > 2) {
            result.setExtraInfo(values[2]);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransportHeader that = (TransportHeader) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(encryptionAlias, that.encryptionAlias) &&
                Objects.equals(extraInfo, that.extraInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, encryptionAlias, extraInfo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncryptionAlias() {
        return encryptionAlias;
    }

    public void setEncryptionAlias(String encryptionAlias) {
        this.encryptionAlias = encryptionAlias;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}