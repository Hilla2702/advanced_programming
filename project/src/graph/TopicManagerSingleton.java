package graph;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TopicManagerSingleton {

    public static class TopicManager {
        private static final TopicManager instance = new TopicManager();
        private ConcurrentMap<String, Topic> topics;

        private TopicManager() {
            topics = new ConcurrentHashMap<>();
        }

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
