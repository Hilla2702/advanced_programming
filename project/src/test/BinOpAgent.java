package test;

import java.util.function.BinaryOperator;

import test.TopicManagerSingleton.TopicManager;

public class BinOpAgent implements Agent {
    private String agentName; // The agent's name
    private String topicInput1; // The name of the first input topic
    private String topicInput2; // The name of the second input topic
    private String topicOutput; // The name of the topic output
    private BinaryOperator<Double> operation; // Expression of binary operator
    private TopicManager topicManager;

    private Message inputMessage1 = null;
    private Message inputMessage2 = null;

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

    private void subscribeToTopic(String topicName) {
        Topic topic = topicManager.getTopic(topicName);
        topic.subscribe(this);
    }

    // Method to handle callbacks from topics
    @Override
    public void callback(String topicName, Message message) {
        if (topicName.equals(topicInput1)) {
            inputMessage1 = message;
        } else if (topicName.equals(topicInput2)) {
            inputMessage2 = message;
        }
        execute();
    }

    // Method to execute the binary operation if both input messages are available
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

    // Method to reset the input messages
    @Override
    public void reset() {
        inputMessage1 = null;
        inputMessage2 = null;
    }

    // Method to close the agent and unsubscribe from topics
    @Override
    public void close() {
        topicManager.getTopic(topicInput1).unsubscribe(this);
        topicManager.getTopic(topicInput2).unsubscribe(this);
    }

    @Override
    public String getName() {
        return agentName;
    }

    // Getters & Setters
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getTopicInput1() {
        return topicInput1;
    }

    public void setTopicInput1(String topicInput1) {
        this.topicInput1 = topicInput1;
    }

    public String getTopicInput2() {
        return topicInput2;
    }

    public void setTopicInput2(String topicInput2) {
        this.topicInput2 = topicInput2;
    }

    public String getTopicOutput() {
        return topicOutput;
    }

    public void setTopicOutput(String topicOutput) {
        this.topicOutput = topicOutput;
    }

    public BinaryOperator<Double> getOperation() {
        return operation;
    }

    public void setOperation(BinaryOperator<Double> operation) {
        this.operation = operation;
    }
}
