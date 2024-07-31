package graph;

import java.util.Objects;

// Implements an agent that increments a received number and publishes the result
public class IncAgent implements Agent {
    private String[] subs; // Array of subscription topics
    private String[] pubs; // Array of publication topics
    private String topic; // The main subscription topic
    private String output_topic; // The output topic for publishing results
    private double inc_num; // Number to be incremented

    // Constructor to initialize the agent with subscription and publication topics
    public IncAgent(String[] subs, String[] pubs) {
        this.reset();
        this.subs = subs;
        this.pubs = pubs;

        // Subscribe to the first input topic if available
        if (subs != null && subs.length > 0) {
            topic = subs[0];
            try {
                TopicManagerSingleton.get().getTopic(topic).subscribe(this);
            } catch (Exception e) {
                System.out.println("Failed to subscribe to topic: " + topic + " for Agent inc");
            }
        } else {
            topic = null;
            System.out.println("No subscriptions for Agent- inc");
        }

        // Add the agent as a publisher to the first output topic if available
        if (pubs != null && pubs.length > 0) {
            output_topic = pubs[0];
            try {
                TopicManagerSingleton.get().getTopic(this.output_topic).addPublisher(this);
            } catch (Exception e) {
                System.out.println("Failed to subscribe to topic: " + output_topic + " for Agent inc");
            }
        } else {
            output_topic = null;
            System.out.println("No publications for Agent- inc");
        }
    }

    // Handles incoming messages and processes them
    @Override
    public void callback(String topic, Message msg) {
        if (Double.isNaN(msg.asDouble)) {
            return; // Ignore messages with NaN values
        }
        if (topic.equals(this.topic)) {
            this.inc_num = msg.asDouble;
            if (this.output_topic != null) {
                TopicManagerSingleton.get().getTopic(this.output_topic).publish(new Message(this.inc_num + 1));
            }
        }
    }

    // Closes the agent and unsubscribes from topics
    @Override
    public void close() {
        if (this.topic != null) {
            TopicManagerSingleton.get().getTopic(this.topic).unsubscribe(this);
        }
        if (this.output_topic != null) {
            TopicManagerSingleton.get().getTopic(this.output_topic).removePublisher(this);
        }
    }

    // Resets the increment number to zero
    @Override
    public void reset() {
        inc_num = 0;
    }

    // Gets the name of the agent
    @Override
    public String getName() {
        return "IncAgent";
    }

    // Checks equality based on the topic and output topic
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        IncAgent other = (IncAgent) obj;

        return Objects.equals(this.topic, other.topic) && Objects.equals(this.output_topic, other.output_topic);
    }
}
