package graph;

import java.nio.ByteBuffer;
import java.util.Date;

public class Message {
    public final byte[] data; // Raw data as byte array
    public final String asText; // Text representation of the data
    public final double asDouble; // Numeric representation of the data
    public final Date date; // Timestamp of the message creation

    // Constructor for initializing Message with byte array
    public Message(byte[] data) {
        this.data = data;
        this.asText = new String(data); // Convert byte array to text
        double tempDouble;
        try {
            tempDouble = byteArrayToDouble(data); // Convert byte array to double
        } catch (Exception e) {
            tempDouble = Double.NaN; // Handle conversion failure
        }
        this.asDouble = tempDouble;
        this.date = new Date(); // Set current date and time
    }

    // Constructor for initializing Message with text
    public Message(String asText) {
        this(asText.getBytes()); // Convert text to byte array
    }

    // Constructor for initializing Message with double value
    public Message(double asDouble) {
        this(doubleToByteArray(asDouble)); // Convert double to byte array
    }

    // Convert byte array to double
    private static double byteArrayToDouble(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes); // Wrap bytes in ByteBuffer
        return buffer.getDouble(); // Read double from buffer
    }

    // Convert double to byte array
    private static byte[] doubleToByteArray(double value) {
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES); // Allocate buffer for double
        buffer.putDouble(value); // Write double to buffer
        return buffer.array(); // Return byte array representation
    }
}
