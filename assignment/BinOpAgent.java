package test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import test.Message;

public class BinOpAgent {
    private String agentName;
    private String topicInput1;
    private String topicInput2;
    private String topicOutput;
    private BinaryOperator<Double> operation;
    private Map<String, Message> topics;

    public BinOpAgent(String agentName, String topicInput1, String topicInput2, String topicOutput,
            BinaryOperator<Double> operation) {
        this.agentName = agentName;
        this.topicInput1 = topicInput1;
        this.topicInput2 = topicInput2;
        this.topicOutput = topicOutput;
        this.operation = operation;
        this.topics = new HashMap<>();

        // Initialize topics with default messages
        topics.put(topicInput1, new Message(0.0));
        topics.put(topicInput2, new Message(0.0));
        topics.put(topicOutput, new Message(0.0));
    }

    // Method to register the agent to topics
    public void register(String topic, Message message) {
        topics.put(topic, message);
    }

    // Method to execute the binary operation if both input messages are available
    public void execute() {
        Message input1 = topics.get(topicInput1);
        Message input2 = topics.get(topicInput2);

        if (input1 != null && input2 != null) {
            double result = operation.apply(input1.asDouble, input2.asDouble);
            Message outputMessage = new Message(result);
            topics.put(topicOutput, outputMessage);
        }
    }

    // Getters and Setters
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

    public Map<String, Message> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, Message> topics) {
        this.topics = topics;
    }
}
