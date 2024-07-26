package test;

//import test.TopicManagerSingleton.TopicManager;

public class IncAgent implements Agent {
    private String[] subs;
    private String[] pubs;

    public IncAgent(String[] subs, String[] pubs) {
        this.subs = subs;
        this.pubs = pubs;
        TopicManagerSingleton.get().getTopic(subs[0]).subscribe(this);
    }

    @Override
    public void callback(String topic, Message msg) {
        double value = msg.asDouble;
        value++;
        TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(value));
    }

    @Override
    public void close() {
    }

    @Override
    public void reset() {
    }

    @Override
    public String getName() {
        return "IncAgent";
    }
}
