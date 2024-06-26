package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import test.TopicManagerSingleton.TopicManager;

public class Graph extends ArrayList<Node> {

    public boolean hasCycle() {
        Set<Node> visited = new HashSet<>();
        Set<Node> DfsPath = new HashSet<>();
        return dfsCycleCheck(this, visited, DfsPath);
    }

    public boolean dfsCycleCheck(Node node, Set<Node> visited, Set<Node> DfsPath) {
        if (DfsPath.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }
        visited.add(node);
        DfsPath.add(node);
        for (Node neighbor : node.edges()) {
            if (dfsCycleCheck(neighbor, visited, DfsPath)) {
                return true;
            }
        }
        DfsPath.remove(node);
        return false;
    }

    public void createFromTopics(TopicManager topicManager) {
        for (String topicName : topicManager.getAllTopics()) {
            Node topicNode = new Node("T_" + topicName);
            for (Agent agent : topicManager.getSubscribers(topicName)) {
                topicNode.addEdge(new Node("A_" + agent.getName()));
            }
            for (String publishedTopic : topicManager.getPublications(agent.getName())) {
                topicNode.addEdge(new Node("T_" + publishedTopic));
            }
            this.add(topicNode);
        }
    }

}
