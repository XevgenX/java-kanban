package kanban.impl;

import kanban.model.Task;


public class Node {
    private Task task;
    private Node previousNode;
    private Node nextNode;

    public Node(Task task, Node previousNode) {
        this.task = task;
        this.previousNode = previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public Task getTask() {
        return task;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public boolean hasPrevious() {
        return previousNode != null;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public boolean hasNext() {
        return nextNode != null;
    }
}
