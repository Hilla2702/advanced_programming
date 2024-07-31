package graph;

import java.util.HashSet;
import java.util.Set;

public class Topic {
    public final String name; // Name of the topic
    private Set<Agent> subs = new HashSet<>(); // Set of subscribers to this topic
    private Set<Agent> pubs = new HashSet<>(); // Set of publishers for this topic

    // Constructor initializes the topic with a name
    Topic(String name) {
        this.name = name;
    }

    // Add an agent as a subscriber to this topic
    public void subscribe(Agent a) {
        subs.add(a); // Add the agent to the subscribers set
    }

    // Remove an agent from the list of subscribers
    public void unsubscribe(Agent a) {
        if (subs.contains(a)) {
            subs.remove(a); // Remove the agent from the subscribers set if present
        }
    }

    // Publish a message to all subscribers
    public void publish(Message m) {
        for (Agent agent : subs) {
            agent.callback(name, m); // Call back each subscriber with the topic name and message
        }
    }

    // Add an agent as a publisher for this topic
    public void addPublisher(Agent a) {
        pubs.add(a); // Add the agent to the publishers set
    }

    // Remove an agent from the list of publishers
    public void removePublisher(Agent a) {
        if (pubs.contains(a)) {
            pubs.remove(a); // Remove the agent from the publishers set if present
        }
    }

    // Get the set of subscribers for this topic
    public Set<Agent> getSubscribers() {
        return subs; // Return the set of subscribers
    }

    // Get the set of publishers for this topic
    public Set<Agent> getPublishers() {
        return pubs; // Return the set of publishers
    }
}
