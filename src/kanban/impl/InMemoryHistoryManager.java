package kanban.impl;

import kanban.HistoryManager;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Long, Node> map = new HashMap<>();
    private Node firstNode;
    private Node lastNode;

    @Override
    public void add(Task task) {
        if(isEmpty()) {
            firstNode = new Node(cloneTask(task), null);
            lastNode = firstNode;
        } else {
            remove(task.getId());
            linkLast(task);
        }
        map.put(lastNode.getTask().getId(), lastNode);
    }

    @Override
    public void remove(Long id) {
        if (map.containsKey(id)) {
            removeNode(map.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        if (isEmpty()) {
            return Collections.emptyList();
        } else {
            return getTasks();
        }
    }

    private void linkLast(Task task) {
        Node newLastNode = new Node(cloneTask(task), lastNode);
        lastNode.setNextNode(newLastNode);
        lastNode = newLastNode;
    }

    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node currentNode = lastNode;
        history.add(currentNode.getTask());
        while(currentNode.hasPrevious()) {
            currentNode = currentNode.getPreviousNode();
            history.add(currentNode.getTask());
        }
        return history;
    }

    private void removeNode(Node node) {
        Node previousNode = node.getPreviousNode();
        Node nextNode = node.getNextNode();
        if (previousNode != null) {
            previousNode.setNextNode(nextNode);
        }
        if (nextNode != null) {
            nextNode.setPreviousNode(previousNode);
        }
        map.remove(node.getTask().getId());
    }

    private boolean isEmpty() {
        return firstNode == null;
    }

    private Task cloneTask(Task task) {
        Task clonedTask = null;
        if (task instanceof SubTask) {
            clonedTask = new SubTask((SubTask) task);
        } else if (task instanceof Epic) {
            clonedTask = new Epic((Epic) task);
        } else {
            clonedTask = new Task(task);
        }
        clonedTask.setId(task.getId());
        return clonedTask;
    }
}
