package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    private String name; // The name of the node
    private List<Node> edges; // The list of edges that go from node to others nodes
    private Message message; // The messages that this node can have

    // Constructor, get the name of the node and init all the parameters
    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.message = null;
    }

    // Get & Set of name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Get & Set of edges
    public List<Node> getEdges() {
        return edges;
    }

    public void setEdges(List<Node> edges) {
        this.edges = edges;
    }

    // Get & Set of message
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    // Add edge to edges list
    public void addEdge(Node node) {
        this.edges.add(node);
    }

    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> stack = new HashSet<>();
        return (hasCycles(visited, stack));
    }

    // Checking if the graph has cycles
    public boolean hasCycles(Set<Node> visited, Set<Node> inStack) {
        if (inStack.contains(this)) {
            return true;
        }
        if (visited.contains(this)) {
            return false;
        }
        visited.add(this);
        inStack.add(this);

        for (Node neighbor : edges) {
            if (neighbor.hasCycles(visited, inStack)) {
                return true;
            }
        }
        inStack.remove(this);
        return false;
    }
}
