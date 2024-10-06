package services.history;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryService implements HistoryService {
    private final HashMap<Integer, Node> history = new HashMap<>();

    private static class Node {
        Task task;
        Node prev;
        Node next;
        public Node(Task task) {
            this.task = task;
            this.prev = null;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(task);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
            newNode.prev = oldTail;
        }

        history.put(task.getId(), newNode);
    }

    public void removeNode(Node node) {
        final Node next = node.next;
        final Node prev = node.prev;
        node.task = null;

        if (head == node && tail == node) {
            head = null;
            tail = null;
        } else if (head == node) {
            head = next;
            head.prev = null;
        } else if (tail == node) {
            tail = prev;
            tail.next = null;
        } else {
            prev.next = next;
            next.prev = prev;
        }
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = history.get(id);
        if (nodeToRemove == null) {
            return;
        }

        history.remove(id);
        removeNode(nodeToRemove);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();

        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }

        return tasks;
    }
}
