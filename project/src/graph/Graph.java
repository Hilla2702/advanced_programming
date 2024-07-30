package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph extends ArrayList<Node> {

    // Method to check if the graph has cycles
    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> stack = new HashSet<>();

        for (Node node : this) {
            if (node.hasCycles(visited, stack)) {
                return true;
            }
        }
        return false;
    }

    // Method to create a graph from topics
    public void createFromTopics() {
        // Get all topics from the TopicManager
        Collection<Topic> topics = TopicManagerSingleton.get().getTopics();

        // Create a map to store agents and their corresponding nodes
        Map<Agent, Node> agentNodeMap = new HashMap<>();

        // Iterate through all topics
        for (Topic topic : topics) {
            // Create a node for the topic
            String topicName = "T" + topic.name;
            Node topicNode = new Node(topicName);
            this.add(topicNode);

            // Process publishers
            for (Agent publisher : topic.getPublishers()) {
                Node publisherNode = agentNodeMap.computeIfAbsent(publisher, a -> {
                    Node node = new Node("A" + a.getName());
                    this.add(node);
                    return node;
                });
                publisherNode.addEdge(topicNode);
            }

            // Process subscribers
            for (Agent subscriber : topic.getSubscribers()) {
                Node subscriberNode = agentNodeMap.computeIfAbsent(subscriber, a -> {
                    Node node = new Node("A" + a.getName());
                    this.add(node);
                    return node;
                });
                topicNode.addEdge(subscriberNode);
            }
        }
    }

}
