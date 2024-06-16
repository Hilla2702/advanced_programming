package test;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import project_biu.graph.Topic;

public class TopicManagerSingleton {

    public static class TopicManager {
        private static final TopicManager instance = new TopicManager();
        private final ConcurrentMap<String, Topic> topics = new ConcurrentHashMap<>();

        public Topic getTopic(String name) {
            return topics.computeIfAbsent(name, k -> new Topic(name));
        }

        public Collection<Topic> getTopics() {
            return topics.values();
        }

        public void clear() {
            topics.clear();
        }

    }

    public static TopicManager get() {
        return TopicManager.instance;
    }

}
