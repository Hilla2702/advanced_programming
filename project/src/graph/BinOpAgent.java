package graph;

import java.util.function.BinaryOperator;

import graph.TopicManagerSingleton.TopicManager;

public class BinOpAgent implements Agent {
    private String agentName; // The agent's name
    private String topicInput1; // The name of the first input topic
    private String topicInput2; // The name of the second input topic
    private String topicOutput; // The name of the topic output
    private BinaryOperator<Double> operation; // Expression of binary operator
    private TopicManager topicManager; // Topic manager for managing topics

    private Message inputMessage1 = null; // First input message
    private Message inputMessage2 = null; // Second input message

    // Constructor to initialize the agent with necessary parameters
    public BinOpAgent(String agentName, String topicInput1, String topicInput2, String topicOutput,
            BinaryOperator<Double> operation) {
        this.agentName = agentName;
        this.topicInput1 = topicInput1;
        this.topicInput2 = topicInput2;
        this.topicOutput = topicOutput;
        this.operation = operation;
        this.topicManager = TopicManagerSingleton.get();

        // Subscribe to the input topics
        this.subscribeToTopic(topicInput1);
        this.subscribeToTopic(topicInput2);
    }

    // Subscribes to a given topic
    private void subscribeToTopic(String topicName) {
        Topic topic = topicManager.getTopic(topicName);
        topic.subscribe(this);
    }

    // Handles callbacks from topics and processes messages
    @Override
    public void callback(String topicName, Message message) {
        if (topicName.equals(topicInput1)) {
            inputMessage1 = message;
        } else if (topicName.equals(topicInput2)) {
            inputMessage2 = message;
        }
        execute(); // Executes the operation when both messages are available
    }

    // Executes the binary operation if both input messages are available
    private void execute() {
        if (inputMessage1 != null && inputMessage2 != null) {
            double value1 = inputMessage1.asDouble;
            double value2 = inputMessage2.asDouble;

            if (!Double.isNaN(value1) && !Double.isNaN(value2)) {
                double result = operation.apply(value1, value2);
                Message outputMessage = new Message(result);
                Topic outputTopic = topicManager.getTopic(topicOutput);
                outputTopic.publish(outputMessage);
            }
        }
    }

    // Resets the input messages
    @Override
    public void reset() {
        inputMessage1 = null;
        inputMessage2 = null;
    }

    // Closes the agent and unsubscribes from topics
    @Override
    public void close() {
        topicManager.getTopic(topicInput1).unsubscribe(this);
        topicManager.getTopic(topicInput2).unsubscribe(this);
    }

    // Gets the name of the agent
    @Override
    public String getName() {
        return agentName;
    }

    // Getter and setter for agentName
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    // Getter and setter for topicInput1
    public String getTopicInput1() {
        return topicInput1;
    }

    public void setTopicInput1(String topicInput1) {
        this.topicInput1 = topicInput1;
    }

    // Getter and setter for topicInput2
    public String getTopicInput2() {
        return topicInput2;
    }

    public void setTopicInput2(String topicInput2) {
        this.topicInput2 = topicInput2;
    }

    // Getter and setter for topicOutput
    public String getTopicOutput() {
        return topicOutput;
    }

    public void setTopicOutput(String topicOutput) {
        this.topicOutput = topicOutput;
    }

    // Getter and setter for operation
    public BinaryOperator<Double> getOperation() {
        return operation;
    }

    public void setOperation(BinaryOperator<Double> operation) {
        this.operation = operation;
    }
}
