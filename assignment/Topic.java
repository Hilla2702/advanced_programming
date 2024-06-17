package project_biu.graph;

import java.util.HashSet;
import java.util.Set;

import test.Agent;
import test.Message;

public class Topic {
    public final String name;
    private Set<Agent> subs = new HashSet<>();
    private Set<Agent> pubs = new HashSet<>();

    Topic(String name) {
        this.name = name;
    }

    public void subscribe(Agent a) {
        subs.add(a);
    }

    public void unsubscribe(Agent a) {
        if (subs.contains(a)) {
            subs.remove(a);
        }
    }

    public void publish(Message m) {
        for (Agent agent : subs) {
            agent.callback(name, m);
        }
    }

    public void addPublisher(Agent a) {
        pubs.add(a);
    }

    public void removePublisher(Agent a) {
        if (pubs.contains(a)) {
            pubs.remove(a);
        }
    }

}
