package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParallelAgent implements Agent {
    private final Agent agent;
    private final BlockingQueue<Message> messageQueue;

    public ParallelAgent(Agent agent, int capacity) {
        this.agent = agent;
        this.messageQueue = new ArrayBlockingQueue<>(capacity);
        this.processingThread = new Thread(this::processMessages);
        this.processingThread.start();
    }

    public void proprocessMessages() {
        try {
            while (running) {
                Message message = take();
                agent.callback(message.topic, message);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void put(Message message) throws InterruptedException {
        messageQueue.put(message);
    }

    public void callback(String topic, Message msg) {
        try {
            put(new Message(topic, msg));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void close() {
        running = false;
        processingThread.interrupt();
        agent.close();
    }
}
