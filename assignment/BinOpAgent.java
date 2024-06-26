package test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

public class BinOpAgent {
    private String name;
    private String inputTopic1;
    private String inputTopic2;
    private String outputTopic;
    private BinaryOperator<Double> operation;
    private static Map<String, Double> topics = new HashMap<>();

    public BinOpAgent(String name, String inputTopic1, String inputTopic2, String outputTopic,
            BinaryOperator<Double> operation) {
        this.name = name;
        this.inputTopic1 = inputTopic1;
        this.inputTopic2 = inputTopic2;
        this.outputTopic = outputTopic;
        this.operation = operation;

        topics.putIfAbsent(inputTopic1, null);
        topics.putIfAbsent(inputTopic2, null);
    }

    public void callback() {
        Double input1 = topics.get(inputTopic1);
        Double input2 = topics.get(inputTopic2);

        if (input1 != null && input2 != null) {
            Double result = operation.apply(input1, input2);
            topics.put(outputTopic, result);
            System.out.println("Agent " + name + " calculated: " + result + " and published to " + outputTopic);
        }
    }

    public void reset() {
        topics.put(inputTopic1, null);
        topics.put(inputTopic2, null);
    }

    public static void publish(String topic, Double message) {
        topics.put(topic, message);
    }
}
