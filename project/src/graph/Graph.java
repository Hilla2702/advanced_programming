package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph extends ArrayList<Node> {

    // Checks if the graph contains cycles
    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>(); // Set of visited nodes
        Set<Node> stack = new HashSet<>(); // Set of nodes currently in the recursion stack

        // Iterate through all nodes in the graph
        for (Node node : this) {
            // If a cycle is detected, return true
            if (node.hasCycles(visited, stack)) {
                return true;
            }
        }
        return false; // No cycles detected
    }

    // Creates a graph from topics
    public void createFromTopics() {
        // Get all topics from the TopicManager
        Collection<Topic> topics = TopicManagerSingleton.get().getTopics();

        // Map to store agents and their corresponding nodes
        Map<Agent, Node> agentNodeMap = new HashMap<>();

        // Iterate through all topics
        for (Topic topic : topics) {
            // Create a node for each topic
            String topicName = "T" + topic.name;
            Node topicNode = new Node(topicName);
            this.add(topicNode);

            // Process publishers of the topic
            for (Agent publisher : topic.getPublishers()) {
                // Create or retrieve a node for the publisher
                Node publisherNode = agentNodeMap.computeIfAbsent(publisher, a -> {
                    Node node = new Node("A" + a.getName());
                    this.add(node);
                    return node;
                });
                // Add an edge from the publisher to the topic node
                publisherNode.addEdge(topicNode);
            }

            // Process subscribers of the topic
            for (Agent subscriber : topic.getSubscribers()) {
                // Create or retrieve a node for the subscriber
                Node subscriberNode = agentNodeMap.computeIfAbsent(subscriber, a -> {
                    Node node = new Node("A" + a.getName());
                    this.add(node);
                    return node;
                });
                // Add an edge from the topic node to the subscriber
                topicNode.addEdge(subscriberNode);
            }
        }
    }

}
