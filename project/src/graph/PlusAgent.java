package graph;

import java.util.Objects;

//import test.TopicManagerSingleton.TopicManager;

public class PlusAgent implements Agent {

    private String[] subs; // Array of subscription topic names
    private String[] pubs; // Array of publication topic names
    private String topic1; // Name of the first input topic
    private String topic2; // Name of the second input topic
    private String output_topic; // Name of the output topic
    private double x = 0; // Value for the first input
    private double y = 0; // Value for the second input

    // Constructor initializes PlusAgent and sets up subscriptions and publications
    public PlusAgent(String[] subs, String[] pubs) {
        this.reset(); // Reset internal values
        this.subs = subs; // Initialize subscription topics
        this.pubs = pubs; // Initialize publication topics

        // Subscribe to the first input topic if available
        if (subs != null && subs.length > 0) {
            topic1 = subs[0];
            try {
                TopicManagerSingleton.get().getTopic(topic1).subscribe(this); // Subscribe to the first input topic
            } catch (Exception e) {
                System.out.println("Failed to subscribe to topic: " + topic1 + " for Agent plus");
            }
        } else {
            topic1 = null;
            System.out.println("No subscriptions for Agent- plus");
        }

        // Subscribe to the second input topic if available
        if (subs != null && subs.length > 1) {
            topic2 = subs[1];
            try {
                TopicManagerSingleton.get().getTopic(topic2).subscribe(this); // Subscribe to the second input topic
            } catch (Exception e) {
                System.out.println("Failed to subscribe to topic: " + topic2 + " for Agent plus");
            }
        } else {
            topic2 = null;
            System.out.println("No subscriptions for Agent- plus");
        }

        // Set up the output topic if available
        if (pubs != null && pubs.length > 0) {
            output_topic = pubs[0];
            try {
                TopicManagerSingleton.get().getTopic(this.output_topic).addPublisher(this); // Add this agent as a
                                                                                            // publisher to the output
                                                                                            // topic
            } catch (Exception e) {
                System.out.println("Failed to publish to topic: " + output_topic + " for Agent plus");
            }
        } else {
            output_topic = null;
            System.out.println("No publications for Agent- plus");
        }
    }

    @Override
    public void callback(String topic, Message msg) {
        if (Double.isNaN(msg.asDouble)) {
            return; // Ignore non-numeric messages
        }
        // Update the values based on the topic
        if (topic.equals(subs[0])) {
            x = msg.asDouble;
        } else if (topic.equals(subs[1])) {
            y = msg.asDouble;
        }

        // Publish the sum if both inputs are received
        if ((x == msg.asDouble || y == msg.asDouble) && this.output_topic != null) {
            double sum = x + y;
            TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(sum)); // Publish the sum to the output
                                                                                     // topic
        }
    }

    @Override
    public void close() {
        // Unsubscribe from the input topics and remove as a publisher from the output
        // topic
        if (this.topic1 != null) {
            TopicManagerSingleton.get().getTopic(subs[0]).unsubscribe(this);
        }
        if (this.topic2 != null) {
            TopicManagerSingleton.get().getTopic(subs[1]).unsubscribe(this);
        }
        if (this.output_topic != null) {
            TopicManagerSingleton.get().getTopic(pubs[0]).removePublisher(this);
        }
    }

    @Override
    public void reset() {
        x = 0; // Reset the value for the first input
        y = 0; // Reset the value for the second input
    }

    @Override
    public String getName() {
        return "PlusAgent"; // Return the name of the agent
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        PlusAgent other = (PlusAgent) obj;

        // Check equality based on input and output topics
        return Objects.equals(this.topic1, other.topic1) &&
                Objects.equals(this.topic2, other.topic2) &&
                Objects.equals(this.output_topic, other.output_topic);
    }
}
