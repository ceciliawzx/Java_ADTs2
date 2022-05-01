package ParallelSets.SequentialSets;

import ParallelSets.Node;
import ParallelSets.SetInterface;

public class SequentialSet<E> implements SetInterface<E> {

  int size = 0;
  private Node<E> head, tail;

  public SequentialSet() {
    head = new SequentialNode<>(null, Integer.MIN_VALUE, null);
    tail = new SequentialNode<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  private Position<E> find(Node<E> start, int key) {
    Node<E> pred, curr = start;
    do {
      pred = curr;
      curr = curr.next();
    } while (curr.key() < key);
    return new Position<>(pred, curr);
  }

  @Override
  public boolean add(E item) {
    Node<E> newNode = new SequentialNode<>(item);
    Position<E> where = find(head, newNode.key());
    if (where.curr.key() == newNode.key()) {
      return false;
    } else {
      newNode.setNext(where.curr);
      where.pred.setNext(newNode);
      size++;
      return true;
    }
  }

  @Override
  public boolean remove(E item) {
    Node<E> newNode = new SequentialNode<>(item);
    Position<E> where = find(head, newNode.key());
    if (where.curr.key() > newNode.key()) {
        return false;
    } else {
        where.pred.setNext(where.curr.next());
        size--;
        return true;
    }
  }

  @Override
  public boolean contains(E item) {
    Node<E> newNode = new SequentialNode<>(item);
    Position<E> expected = find(head, newNode.key());
    return expected.curr.key() == newNode.key();
  }

  @Override
  public int size() {
    return size;
  }

  private static class Position<T> {

    public final Node<T> pred, curr;

    public Position(Node<T> pred, Node<T> curr) {
      this.pred = pred;
      this.curr = curr;
    }
  }
}
