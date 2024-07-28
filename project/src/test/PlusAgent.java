package test;

//import test.TopicManagerSingleton.TopicManager;

public class PlusAgent implements Agent {

    private String[] subs;
    private String[] pubs;
    private String topic1;
    private String topic2;
    private String output_topic;
    private double x = 0;
    private double y = 0;

    public PlusAgent(String[] subs, String[] pubs) {
        this.reset();
        this.subs = subs;
        this.pubs = pubs;

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

        if (subs != null && subs.length > 0) {
            topic2 = subs[1];
            try {
                TopicManagerSingleton.get().getTopic(topic2).subscribe(this); // Subscribe to the first input topic
            } catch (Exception e) {
                System.out.println("Failed to subscribe to topic: " + topic2 + " for Agent plus");
            }
        } else {
            topic2 = null;
            System.out.println("No subscriptions for Agent- plus");
        }

        if (pubs != null && pubs.length > 0) {
            output_topic = pubs[0];
            try {
                TopicManagerSingleton.get().getTopic(this.output_topic).addPublisher(this); // Subscribe to the first
                                                                                            // input topic
            } catch (Exception e) {
                System.out.println("Failed to pubscribe to topic: " + output_topic + " for Agent plus");
            }
        } else {
            output_topic = null;
            System.out.println("No pubscriptions for Agent- plus");
        }
    }

    @Override
    public void callback(String topic, Message msg) {

        if (Double.isNaN(msg.asDouble)) {
            return;
        }
        if (topic.equals(subs[0])) {
            x = msg.asDouble;
        } else if (topic.equals(subs[1])) {
            y = msg.asDouble;
        }

        if ((x == msg.asDouble || y == msg.asDouble) && this.output_topic != null) {
            double sum = x + y;
            TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(sum));
        }
    }

    @Override
    public void close() {
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
        x = 0;
        y = 0;
    }

    @Override
    public String getName() {
        return "PlusAgent";
    }
}
