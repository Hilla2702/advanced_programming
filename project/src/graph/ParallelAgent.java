package graph;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParallelAgent implements Agent {
    private final Agent agent; // Reference to the original Agent
    private final BlockingQueue<Message> queue; // Blocking queue for storing messages
    private boolean running = true; // Flag indicating if the processing thread should keep running
    private final Thread processingThread; // Thread for handling message processing

    // Constructor initializes ParallelAgent with a specified queue capacity
    public ParallelAgent(Agent agent, int capacity) {
        this.agent = agent; // Initialize the reference to the original agent
        this.queue = new ArrayBlockingQueue<>(capacity); // Initialize the queue with the given capacity
        this.processingThread = new Thread(this::processMessages); // Create a thread for processing messages
        this.processingThread.start(); // Start the processing thread
    }

    // Take a message from the queue, waiting if necessary
    public Message take() throws InterruptedException {
        return queue.take(); // Block until a message is available
    }

    // Put a message into the queue, waiting if necessary
    public void put(Message message) throws InterruptedException {
        queue.put(message); // Block until space is available in the queue
    }

    // Handle a callback from a topic
    public void callback(String topic, Message msg) {
        try {
            put(new Message(topic.getBytes())); // Create a message from the topic and put it in the queue
        } catch (InterruptedException e) { // Handle interruption
            Thread.currentThread().interrupt(); // Set interrupt flag for the current thread
        }
    }

    // Close the ParallelAgent
    public void close() {
        running = false; // Stop the processing thread
        processingThread.interrupt(); // Interrupt the processing thread
        agent.close(); // Close the original agent
    }

    // Reset the ParallelAgent
    public void reset() {
        queue.clear(); // Clear the queue
        agent.reset(); // Reset the original agent
    }

    // Get the name of the original agent
    public String getName() {
        return agent.getName(); // Return the name from the original agent
    }

    // Process messages from the queue
    private void processMessages() {
        while (running) { // Keep running while the flag is true
            try {
                Message msg = take(); // Retrieve a message from the queue
                agent.callback(new String(msg.data), msg); // Call the callback method on the original agent
            } catch (InterruptedException e) {
                if (!running) { // Check if the thread should stop running
                    Thread.currentThread().interrupt(); // Set interrupt flag
                }
            }
        }
    }
}
