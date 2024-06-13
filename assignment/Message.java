package test;

import java.util.Date;

public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;

    // constructors
    public Message(byte[] data) {
        this.data = data;
        this.asText = data.toString();
        try {
            this.asDouble = ByteArrayToDouble(data);
        } catch (Exception e) {
            this.asDouble = Double.NaN;
        }
        this.date = new Date();
    }

    public Message(String asText) {
        this(asText.getBytes());
    }

    public Message(double asDouble) {
        this(doubleToByteArray(asDouble));
    }
}
