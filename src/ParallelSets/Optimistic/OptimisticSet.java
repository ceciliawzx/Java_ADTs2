package ParallelSets.Optimistic;

import ParallelSets.Node;
import ParallelSets.SetInterface;
import java.util.concurrent.atomic.AtomicInteger;

public class OptimisticSet<E> implements SetInterface<E> {

  protected AtomicInteger size = new AtomicInteger(0);
  protected ReadWriteNode<E> head, tail;

  public OptimisticSet() {
    head = new ReadWriteNode<>(null, Integer.MIN_VALUE, null);
    tail = new ReadWriteNode<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  private boolean valid(Node<E> pred, Node<E> curr) {
    Node<E> node = head;
    while (node.key() <= pred.key()) {
      if (node == pred) {
        return pred.next() == curr;
      }
      node = node.next();
    }
    return false;
  }

  private Position<E> find(ReadWriteNode<E> start, int key) {
    ReadWriteNode<E> pred, curr;
    curr = start;
    do {
      pred = curr;
      curr = (ReadWriteNode<E>) curr.next();
    } while (curr.key() < key);
    return new Position<>(pred, curr);
  }

  @Override
  public boolean add(E item) {
    ReadWriteNode<E> newNode = new ReadWriteNode<>(item);
    do {
      Position<E> where = find(head, newNode.key());
      ReadWriteNode<E> pred = where.pred, curr = where.curr;
      pred.lock();
      curr.lock();
      try {
        if (valid(pred, curr)) {
          if (curr.key() == newNode.key()) {
            return false;
          } else {
            newNode.setNext(curr);
            pred.setNext(newNode);
            size.incrementAndGet();
            return true;
          }
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    } while (true);
  }

  @Override
  public boolean remove(E item) {
    ReadWriteNode<E> newNode = new ReadWriteNode<>(item);
    do {
      Position<E> where = find(head, newNode.key());
      ReadWriteNode<E> pred = where.pred, curr = where.curr;
      pred.lock();
      curr.lock();
      try {
        if (valid(pred, curr)) {
          if (curr.key() > curr.key()) {
            return false;
          } else {
            pred.setNext(curr.next());
            size.decrementAndGet();
            return true;
          }
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    } while (true);
  }

  @Override
  public boolean contains(E item) {
    ReadWriteNode<E> newNode = new ReadWriteNode<>(item);
    do {
      Position<E> expected = find(head, newNode.key());
      ReadWriteNode<E> pred = expected.pred, curr = expected.curr;
      pred.lock();
      curr.lock();
      try {
        if (valid(pred, curr)) {
          return curr.key() == newNode.key();
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    } while (true);
  }

  @Override
  public int size() {
    return size.get();
  }

  private static class Position<T> {
    public final ReadWriteNode<T> pred, curr;

    public Position(ReadWriteNode<T> pred, ReadWriteNode<T> curr) {
      this.pred = pred;
      this.curr = curr;
    }
  }
}
