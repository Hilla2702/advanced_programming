package test;

import java.util.Objects;

//import test.TopicManagerSingleton.TopicManager;

public class IncAgent implements Agent {
    private String[] subs;
    private String[] pubs;
    private String topic;
    private String output_topic;
    private double inc_num;

    public IncAgent(String[] subs, String[] pubs) {
        this.reset();
        this.subs = subs;
        this.pubs = pubs;
        if (subs != null && subs.length > 0) {
            topic = subs[0];
            try {
                TopicManagerSingleton.get().getTopic(topic).subscribe(this); // Subscribe to the first input topic
            } catch (Exception e) {
                System.out.println("Failed to subscribe to topic: " + topic + " for Agent inc");
            }
        } else {
            topic = null;
            System.out.println("No subscriptions for Agent- inc");
        }

        if (pubs != null && pubs.length > 0) {
            output_topic = pubs[0];
            try {
                TopicManagerSingleton.get().getTopic(this.output_topic).addPublisher(this); // Subscribe to the first
                                                                                            // input topic
            } catch (Exception e) {
                System.out.println("Failed to pubscribe to topic: " + output_topic + " for Agent inc");
            }
        } else {
            output_topic = null;
            System.out.println("No pubscriptions for Agent- inc");
        }
    }

    @Override
    public void callback(String topic, Message msg) {
        if (Double.isNaN(msg.asDouble)) {
            return;
        }
        if (topic.equals(this.topic)) {
            this.inc_num = msg.asDouble;
            if (this.output_topic != null) {
                TopicManagerSingleton.get().getTopic(this.output_topic).publish(new Message(this.inc_num + 1));
            }
        }
    }

    @Override
    public void close() {
        if (this.topic != null)
            TopicManagerSingleton.get().getTopic(this.topic).unsubscribe(this);
        if (this.output_topic != null)
            TopicManagerSingleton.get().getTopic(this.output_topic).removePublisher(this);
    }

    @Override
    public void reset() {
        inc_num = 0;
    }

    @Override
    public String getName() {
        return "IncAgent";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        IncAgent other = (IncAgent) obj;

        return Objects.equals(this.topic, other.topic) &&
                Objects.equals(this.output_topic, other.output_topic);
    }
}
