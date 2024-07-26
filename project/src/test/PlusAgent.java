package test;

//import test.TopicManagerSingleton.TopicManager;

public class PlusAgent implements Agent {

    private String[] subs;
    private String[] pubs;
    private double x = 0;
    private double y = 0;

    public PlusAgent(String[] subs, String[] pubs) {
        this.subs = subs;
        this.pubs = pubs;

        TopicManagerSingleton.get().getTopic(subs[0]).subscribe(this);
        TopicManagerSingleton.get().getTopic(subs[1]).subscribe(this);
    }

    @Override
    public void callback(String topic, Message msg) {

        if (topic.equals(subs[0])) {
            x = msg.asDouble;
        } else if (topic.equals(subs[1])) {
            y = msg.asDouble;
        }

        if (x != 0 && y != 0) {
            double sum = x + y;
            TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(sum));
        }
    }

    @Override
    public void close() {
        if (subs[0] != null) {
            TopicManagerSingleton.get().getTopic(subs[0]).unsubscribe(this);
        }
        if (subs[1] != null) {
            TopicManagerSingleton.get().getTopic(subs[1]).unsubscribe(this);
        }
        if (pubs[0] != null) {
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
