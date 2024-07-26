package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph extends ArrayList<Node> {

    // Method to check if the graph has cycles
    public boolean hasCycles() {
        for (Node node : this) {
            if (node.hasCycles()) {
                return true;
            }
        }
        return false;
    }

    // Method to create a graph from topics
    public void createFromTopics() {
        TopicManagerSingleton.TopicManager topicManager = TopicManagerSingleton.get();
        Map<String, Node> nodesMap = new HashMap<>();

        // Create nodes for all topics and add edges to subscribers
        for (Topic topic : topicManager.getTopics()) {
            String topicName = "T" + topic.name;
            Node topicNode = nodesMap.computeIfAbsent(topicName, Node::new);
            this.add(topicNode);

            // Add edges from the topic to its subscribers
            for (Agent agent : topic.getSubscribers()) {
                String agentName = "A" + agent.getName();
                Node agentNode = nodesMap.computeIfAbsent(agentName, Node::new);
                this.add(agentNode);
                topicNode.addEdge(agentNode);
            }
        }

        // Add edges from agents to topics they publish to
        for (Topic topic : topicManager.getTopics()) {
            for (Agent agent : topic.getPublishers()) {
                String agentName = "A" + agent.getName();
                Node agentNode = nodesMap.get(agentName);
                if (agentNode == null) {
                    agentNode = new Node(agentName);
                    nodesMap.put(agentName, agentNode);
                    this.add(agentNode);
                }

                for (Topic pubTopic : topicManager.getTopics()) {
                    if (pubTopic.getPublishers().contains(agent)) {
                        String pubTopicName = "T" + pubTopic.name;
                        Node pubTopicNode = nodesMap.computeIfAbsent(pubTopicName, Node::new);
                        this.add(pubTopicNode);
                        agentNode.addEdge(pubTopicNode);
                    }
                }
            }
        }
    }
}
