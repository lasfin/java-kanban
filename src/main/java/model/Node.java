package model;

public class Node {
    private Task task;
    private Node prev;
    private Node next;

    public Node(Node prev, Task task, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
