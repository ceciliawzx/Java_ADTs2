package ParallelSets.Finegrained;

import ParallelSets.Node;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockableNode<E> implements Node<E> {

    private Lock lock = new ReentrantLock();

    private E item;
    private int key;
    private LockableNode<E> next;

    public LockableNode(E item, int key, LockableNode<E> next) {
        this.item = item;
        this.key = key;
        this.next = next;
    }

    public LockableNode(E item, LockableNode<E> next) {
        this(item, item.hashCode(), next);
    }

    public LockableNode(E item) {
        this(item, null);
    }

    @Override
    public E item() {
        return item;
    }

    @Override
    public int key() {
        return key;
    }

    @Override
    public LockableNode<E> next() {
        return  next;
    }

    @Override
    public void setItem(E item) {
        this.item = item;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public void setNext(Node<E> next) {
        this.next = (LockableNode<E>) next;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

}
