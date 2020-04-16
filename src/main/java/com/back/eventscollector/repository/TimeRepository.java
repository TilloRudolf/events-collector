package com.back.eventscollector.repository;

import com.back.eventscollector.model.EventItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("timeRepository")
public class TimeRepository {
    private final List<TimeRepositoryNode> nodes;

    public TimeRepository() {
        nodes = initNodesList();
    }

    public TimeRepositoryNode getNode(TimesEnum name) {
        TimeRepositoryNode node = null;
        for (TimeRepositoryNode n : nodes) {
            if (n.getTime() == name) {
                node = n;
            }
        }
        return node;
    }

    private static List<TimeRepositoryNode> initNodesList() {
        List<TimeRepositoryNode> nodes = createNodeList();
        fillNodesList(nodes);
        return nodes;
    }

    private static List<TimeRepositoryNode> createNodeList() {
        List<TimeRepositoryNode> nodes = new ArrayList<>();
        int index = 0;
        for (TimesEnum e : TimesEnum.values()) {
            TimeRepositoryNode previousNode;
            if (nodes.isEmpty()) {
                previousNode = null;
            } else {
                previousNode = nodes.get(index++);
            }
            nodes.add(new TimeRepositoryNode(previousNode, e));
        }
        return nodes;
    }

    private static void fillNodesList(List<TimeRepositoryNode> nodes) {
        int lastIndex = nodes.size();
        int count = 0;
        for (TimeRepositoryNode node : nodes) {
            TimeRepositoryNode nextNode;
            if (++count == lastIndex) {
                nextNode = null;
            } else {
                nextNode = nodes.get(count);
            }
            node.setNextNode(nextNode);
        }
    }

    private final Map<Long, EventItem> events = new ConcurrentHashMap<>();

    public void recalculateNodes() {
        nodes.forEach(this::recalculateNode);
    }

    private void recalculateNode(TimeRepositoryNode node) {
        node.getEvents().forEach((k, v) -> {
            long timeBefore = System.currentTimeMillis() - node.getTime().getTimeRange().getValue();
            boolean outOfRange = v.getEvent().getTimestampMillisUTC() < timeBefore;
            if (outOfRange) {
                if (node.getNextNode() != null) node.getNextNode().putEvent(k, v);
                node.removeEvent(k);
            }
        });
    }

    public EventItem get(Long key) {
        return events.get(key);
    }

    public void put(Long key, EventItem value) {
        events.put(key, value);
    }

    public Map<Long, EventItem> getEvents() {
        return events;
    }
}
