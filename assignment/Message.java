package test;

import java.nio.ByteBuffer;
import java.util.Date;

public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;

    // constructors
    public Message(byte[] data) {
        this.data = data;
        this.asText = new String(data);
        double tempDouble;
        try {
            tempDouble = byteArrayToDouble(data);
        } catch (Exception e) {
            tempDouble = Double.NaN;
        }
        this.asDouble = tempDouble;
        this.date = new Date();
    }

    public Message(String asText) {
        this(asText.getBytes());
    }

    public Message(double asDouble) {
        this(doubleToByteArray(asDouble));
    }

    // convert byte array to double
    private static double byteArrayToDouble(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getDouble();
    }

    // convert double to byte array
    private static byte[] doubleToByteArray(double value) {
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
        buffer.putDouble(value);
        return buffer.array();
    }
}
