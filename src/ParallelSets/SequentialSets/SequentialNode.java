package ParallelSets.SequentialSets;

import ParallelSets.Node;

public class SequentialNode<T> implements Node<T> {

    private T item;
    private int key;
    private Node<T> next;

    public SequentialNode(T item, int key, Node<T> next) {
        this.item = item;
        this.key = key;
        this.next = next;
    }

    public SequentialNode(T item, Node<T> next) {
        this(item, item.hashCode(), next);
    }

    public SequentialNode(T item) {
        this(item, null);
    }

    @Override
    public T item() {
        return item;
    }

    @Override
    public int key() {
        return key;
    }

    @Override
    public Node<T> next() {
        return next;
    }

    @Override
    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public void setNext(Node<T> next) {
        this.next = next;
    }
}
