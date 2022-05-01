package ParallelSets.Coarsegrained;

import ParallelSets.SequentialSets.SequentialSet;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseSet<E> extends SequentialSet<E> {

  private Lock lock = new ReentrantLock();

  @Override
  public boolean contains(E item) {
    lock.lock();
    try {
      return super.contains(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean add(E item) {
    lock.lock();
    try {
      return super.add(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean remove(E item) {
    lock.lock();
    try {
      return super.remove(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int size() {
    lock.lock();
    try {
      return super.size();
    } finally {
      lock.unlock();
    }
  }
}
