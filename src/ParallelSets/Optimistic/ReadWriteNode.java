package ParallelSets.Optimistic;

import ParallelSets.Node;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteNode<E> implements Node<E> {

  private Lock lock = new ReentrantLock();
  private E item;
  private int key;
  private volatile ReadWriteNode<E> next;

  public ReadWriteNode(E item, int key, ReadWriteNode<E> next) {
      this.item = item;
      this.key = key;
      this.next = next;
  }

  public ReadWriteNode(E item, ReadWriteNode<E> next) {
      this(item, item.hashCode(), next);
  }

  public ReadWriteNode(E item) {
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
  public Node<E> next() {
    return next;
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
    this.next = (ReadWriteNode<E>) next;
  }

  public void lock() {
      lock.lock();
  }

  public void unlock() {
      lock.unlock();
  }
}
