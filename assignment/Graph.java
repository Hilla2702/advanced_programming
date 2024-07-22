package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import test.Agent;
import test.Topic;
import test.TopicManagerSingleton;

public class Graph extends ArrayList<Node> {

    // Check if the graph has cycles
    public boolean hasCycles() {
        for (Node node : this) {
            if (node.hasCycles()) {
                return true;
            }
        }
        return false;
    }

    // Initialize the graph from topics
    public void createFromTopics(TopicManagerSingleton.TopicManager topicManager) {
        Map<String, Node> nodes = new HashMap<>();

        // Create nodes for each topic
        for (Topic topic : topicManager.getTopics()) {
            nodes.put(topic.name, new Node(topic.name));
        }

        // Create edges based on agents' subscriptions and publications
        for (Topic topic : topicManager.getTopics()) {
            Node topicNode = nodes.get(topic.name);
            for (Agent agent : topic.getSubscribers()) {
                Node agentNode = nodes.computeIfAbsent(agent.getName(), k -> new Node(agent.getName()));
                topicNode.addEdge(agentNode);
            }
            for (Agent pubAgent : topic.getPublishers()) {
                Node pubAgentNode = nodes.computeIfAbsent(pubAgent.getName(), k -> new Node(pubAgent.getName()));
                pubAgentNode.addEdge(topicNode);
            }
        }

        // Add all nodes to the graph
        this.addAll(nodes.values());
    }
}
