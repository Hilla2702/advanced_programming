package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;

    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.msg = null;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setEdges(List<Node> edges) {
        this.edges = edges;
    }

    public void setMessage(Message msg) {
        this.msg = msg;
    }

    public string getName() {
        return this.name;
    }

    public List<Node> getEdges() {
        return this.edges;
    }

    public Message getMessage() {
        return this.msg;
    }

    public void addEdge(Node node) {
        if (!edges.contains(node)) {
            edges.add(node);
        }
    }

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
}