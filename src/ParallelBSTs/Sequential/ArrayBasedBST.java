package ParallelBSTs.Sequential;

import ParallelBSTs.BST;

public class ArrayBasedBST<E extends Comparable<E>> implements BST<E> {

  private static final int DEFAULT_CAPACITY = 1024;
  private int size;
  private E[] elements;

  public ArrayBasedBST(int capacity) {
    this.size = 0;
    elements = (E[]) new Comparable[capacity];
  }

  public ArrayBasedBST() {
    this(DEFAULT_CAPACITY);
  }

  @Override
  public boolean add(E element) {
    return add(0, element);
  }

  private boolean add(int position, E element) {
    if (position >= elements.length) {
      throw new ArrayIndexOutOfBoundsException("index out of bound");
    }
    if (elements[position] == null) {
      elements[position] = element;
      size++;
      return true;
    } else {
      if (element.compareTo(elements[position]) == 0) {
        return false; // element already in the tree;
      } else if (element.compareTo(elements[position]) < 0) {
        int leftChildPos = getLeftChildIndex(position);
        return add(leftChildPos, element);
      } else {
        int rightChildPos = getRightChildIndex(position);
        return add(rightChildPos, element);
      }
    }
  }

  @Override
  public boolean remove(E element) {
    return remove(0, element);
  }

  private boolean remove(int position, E element) {
    assert element != null;
    if (position >= elements.length || elements[position] == null) {
      return false;
    } else {
      if (element.compareTo(elements[position]) == 0) {
        elements[position] = null;
        size--;
        return true;
      } else if (element.compareTo(elements[position]) < 0) {
        int leftChildPos = getLeftChildIndex(position);
        return remove(leftChildPos, element);
      } else {
        int rightChildPos = getRightChildIndex(position);
        return remove(rightChildPos, element);
      }
    }
  }

  @Override
  public boolean contains(E element) {
    return contains(0, element);
  }

  private boolean contains(int position, E element) {
    assert element != null;
    if (position >= elements.length || elements[position] == null) {
      return false;
    } else {
      if (element.compareTo(elements[position]) == 0) {
        return true;
      } else if (element.compareTo(elements[position]) < 0) {
        int leftChildPos = getLeftChildIndex(position);
        return contains(leftChildPos, element);
      } else {
        int rightChildPos = getRightChildIndex(position);
        return contains(rightChildPos, element);
      }
    }
  }

  private int getLeftChildIndex(int root) {
    return root * 2 + 1;
  }

  private int getRightChildIndex(int root) {
    return root * 2 + 2;
  }
}
