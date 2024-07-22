
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import test.Agent;
import test.TopicManagerSingleton.TopicManager;

public class Graph extends ArrayList<Node> {

    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> dfsPath = new HashSet<>();
        for (Node node : this) {
            if (dfsCycleCheck(node, visited, dfsPath)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfsCycleCheck(Node node, Set<Node> visited, Set<Node> dfsPath) {
        if (dfsPath.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }
        visited.add(node);
        dfsPath.add(node);
        for (Node neighbor : node.getEdges()) {
            if (dfsCycleCheck(neighbor, visited, dfsPath)) {
                return true;
            }
        }
        dfsPath.remove(node);
        return false;
    }

    public void createFromTopics(TopicManager topicManager) {
        for (String topicName : topicManager.getAllTopics()) {
            Node topicNode = new Node("T_" + topicName);
            for (Agent agent : topicManager.getSubscribers(topicName)) {
                topicNode.addEdge(new Node("A_" + agent.getName()));
            }
            for (String publishedTopic : topicManager.getPublications(topicName)) {
                topicNode.addEdge(new Node("T_" + publishedTopic));
            }
            this.add(topicNode);
        }
    }
}
