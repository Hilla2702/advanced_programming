package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    private String name; // The name of the node
    private List<Node> edges; // List of edges connecting this node to other nodes
    private Message message; // Message associated with this node

    // Constructor initializes the node with a name and empty edges list
    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.message = null;
    }

    // Get and set the name of the node
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Get and set the list of edges
    public List<Node> getEdges() {
        return edges;
    }

    public void setEdges(List<Node> edges) {
        this.edges = edges;
    }

    // Get and set the message associated with this node
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    // Add an edge to the list of edges
    public void addEdge(Node node) {
        this.edges.add(node);
    }

    // Check if the graph has cycles starting from this node
    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>(); // Set of visited nodes
        Set<Node> stack = new HashSet<>(); // Set of nodes currently in the recursion stack
        return hasCycles(visited, stack); // Start cycle detection
    }

    // Check if the graph has cycles using DFS
    public boolean hasCycles(Set<Node> visited, Set<Node> inStack) {
        if (inStack.contains(this)) {
            return true; // Cycle detected
        }
        if (visited.contains(this)) {
            return false; // Node already processed, no cycle from this node
        }
        visited.add(this); // Mark node as visited
        inStack.add(this); // Add node to recursion stack

        // Recurse for all neighbors
        for (Node neighbor : edges) {
            if (neighbor.hasCycles(visited, inStack)) {
                return true; // Cycle detected in neighbor's recursion
            }
        }
        inStack.remove(this); // Remove node from recursion stack
        return false; // No cycle detected from this node
    }
}
