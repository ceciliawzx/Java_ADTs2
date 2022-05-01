package ParallelSets.Finegrained;

import ParallelSets.SetInterface;
import java.util.concurrent.atomic.AtomicInteger;

public class FineSet<E> implements SetInterface<E> {

    AtomicInteger size = new AtomicInteger(0);
    private LockableNode<E> head, tail;

    public FineSet() {
        head = new LockableNode<>(null, Integer.MIN_VALUE, null);
        tail = new LockableNode<>(null, Integer.MAX_VALUE, null);
        head.setNext(tail);
    }

    private Position<E> find(LockableNode<E> start, int key) {
        LockableNode<E> pred, curr;
        pred = start;
        pred.lock();
        curr = start.next();
        curr = curr.next();
        while (curr.key() < key) {
            pred.unlock();
            pred = curr;
            curr = curr.next();
            curr.lock();
        }
        return new Position<>(pred, curr);
    }

    @Override
    public boolean add(E item) {
        LockableNode<E> newNode = new LockableNode<>(item);
        LockableNode<E> pred = null, curr = null;
        try {
            Position<E> where = find(head, newNode.key());
            pred = where.pred;
            curr = where.curr;
            if (curr.key() == newNode.key()) {
                return false;
            } else {
                pred.setNext(newNode);
                newNode.setNext(curr);
                size.incrementAndGet();
                return true;
            }
        } finally{
            pred.unlock();
            curr.unlock();
        }
    }

    @Override
    public boolean remove(E item) {
        LockableNode<E> newNode = new LockableNode<>(item);
        LockableNode<E> pred = null, curr = null;
        try {
            Position<E> where = find(head, newNode.key());
            pred = where.pred;
            curr = where.curr;
            if (where.curr.key() > curr.key()) {
                return false;
            } else {
                pred.setNext(curr.next());
                size.decrementAndGet();
                return true;
            }
        } finally{
            pred.unlock();
            curr.unlock();
        }
    }

    @Override
    public boolean contains(E item) {
        LockableNode<E> newNode = new LockableNode<>(item);
        LockableNode<E> pred = null, curr = null;
        try{
            Position<E> expected = find(head, newNode.key());
            pred = expected.pred;
            curr = expected.curr;
            return curr.key() == newNode.key();
        } finally{
            pred.unlock();
            curr.unlock();
        }
    }

    @Override
    public int size() {
        return size.get();
    }

    private static class Position<T> {
        public final LockableNode<T> pred, curr;

        public Position(LockableNode<T> pred, LockableNode<T> curr) {
            this.pred = pred;
            this.curr = curr;
        }
    }
}
