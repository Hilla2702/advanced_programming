package graph;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TopicManagerSingleton {

    public static class TopicManager {
        private static final TopicManager instance = new TopicManager(); // Singleton instance
        private ConcurrentMap<String, Topic> topics; // Concurrent map of topics by name

        // Private constructor initializes the topics map
        private TopicManager() {
            topics = new ConcurrentHashMap<>();
        }

        // Retrieve a topic by name or create it if it doesn't exist
        public Topic getTopic(String name) {
            return topics.computeIfAbsent(name, k -> new Topic(name)); // Compute and return the topic
        }

        // Get a collection of all topics
        public Collection<Topic> getTopics() {
            return topics.values(); // Return the values of the topics map
        }

        // Clear all topics from the manager
        public void clear() {
            topics.clear(); // Clear the map of all topics
        }
    }

    // Get the singleton instance of TopicManager
    public static TopicManager get() {
        return TopicManager.instance; // Return the singleton instance
    }
}
