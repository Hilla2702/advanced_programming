package graph;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParallelAgent implements Agent {
    private final Agent agent;// refrence to the original Agent
    private final BlockingQueue<Message> queue;// blocking queue for messages
    private boolean running = true;// flag indicating if the thread should run
    private final Thread processingThread;// to handel message processing

    public ParallelAgent(Agent agent, int capacity) {
        this.agent = agent;
        this.queue = new ArrayBlockingQueue<>(capacity);// init the queue with maximum capacity
        this.processingThread = new Thread(this::processMessages);// executes the processMessages method
        this.processingThread.start();
    }

    public Message take() throws InterruptedException {
        // waiting if the queue is empty
        return queue.take();// if not so take message from it
    }

    public void put(Message message) throws InterruptedException {
        // waiting if the queue is full
        queue.put(message);// put a message in it if not
    }

    public void callback(String topic, Message msg) {
        try {
            put(new Message(topic.getBytes()));// new message from the topic and put it into the queue
        } catch (InterruptedException e) {// if interrupted:
            Thread.currentThread().interrupt();// set interrupt flag for teh current thread
        }
    }

    public void close() {
        running = false;// set the running flag to be false
        processingThread.interrupt();
        agent.close();
    }

    public void reset() {
        queue.clear();// reset the queue
        agent.reset();// reset the agent by his method
    }

    public String getName() {
        return agent.getName();// return the name of agent by his method
    }

    private void processMessages() {
        while (running) {// thread run while true
            try {
                Message msg = take();// take from the method take() the message
                agent.callback(new String(msg.data), msg);// call callback with the msg we take
            } catch (InterruptedException e) {
                if (!running) {// if the thread is interrupted-> stop running
                    Thread.currentThread().interrupt();// set interrupt flag
                }
            }
        }
    }
}
