package test;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name;// The name of the node
    private List<Node> edges;// The list of edges that go from node to others nodes
    private Message message;// The masseges that this node can have

    // Constractor, get the name of the node and init all the parameters
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

    // Add edges to edges list
    public void addEdge(Node node) {
        this.edges.add(node);
    }

    // Checking if the graph has cycles
    public boolean hasCycles() {// the main method
        return hasCycles(new ArrayList<>());
    }

    private boolean hasCycles(List<Node> visited) {// the auxiliary method, dfs
        if (visited.contains(this)) {
            return true;
        }
        visited.add(this);
        for (Node edge : edges) {
            if (edge.hasCycles(new ArrayList<>(visited))) {
                return true;
            }
        }
        return false;
    }
}
