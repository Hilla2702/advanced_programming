package graph;

// Represents an agent in a graph-based system.
public interface Agent {

    // Gets the name of the agent.
    String getName();

    // Resets the agent to its initial state.
    void reset();

    // Processes a message based on the given topic.
    void callback(String topic, Message msg);

    // Closes resources used by the agent.
    void close();
}
