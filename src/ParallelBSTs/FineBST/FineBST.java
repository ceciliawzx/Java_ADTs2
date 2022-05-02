package ParallelBSTs.FineBST;

import ParallelBSTs.BST;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineBST<E extends Comparable<E>> implements BST<E> {

  private LockableNode<E> root = null;
  // important!
  private Lock rootLock = new ReentrantLock();

  @Override
  public boolean add(E element) {
    LockableNode<E> curr = null, parent = null;
    rootLock.lock();

    if (root == null) {
      root = new LockableNode<>(element);
      rootLock.unlock();
      return true;
    } else {
      curr = root;
      curr.lock();
      rootLock.unlock();
      int compare = 0;
      while (true) {
        parent = curr;
        compare = curr.element.compareTo(element);
        if (compare == 0) {
          curr.unlock();
          return false;
        } else curr = (compare > 0) ? curr.right : curr.left;
        if (curr == null) {
          break;
        } else {
          curr.lock();
          parent.unlock();
        }
      }
      if (compare > 0) {
        parent.left = new LockableNode<>(element);
      } else parent.right = new LockableNode<>(element);
      parent.unlock();
    }
    return true;
  }

  @Override
  public boolean remove(E element) {
    LockableNode<E> curr = null, parent = null;
    rootLock.lock();

    if (root == null) {
      rootLock.unlock();
      return false;
    } else {
      curr = root;
      parent = curr;
      curr.lock();

      int compare = curr.element.compareTo(element);
      if (compare == 0) {
        // replace if the target is found;
        LockableNode<E> replacement = findReplacement(curr);
        if (replacement != null) {
          replacement.left = curr.left;
          replacement.right = curr.right;
        }

        root = replacement;
        curr.unlock();
        rootLock.unlock();
        return true;
      }

      curr.lock();
      rootLock.unlock();
      int parentCompare = compare;

      while (true) {
        compare = curr.element.compareTo(element);

        if (compare == 0) {
          LockableNode<E> replacement = findReplacement(curr);
          if (parentCompare > 0) {
            parent.left = replacement;
          } else parent.right = replacement;
          if (replacement != null) {
            replacement.left = curr.left;
            replacement.right = curr.right;
          }
          curr.unlock();
          parent.unlock();
          return true;
        } else {
          parent.unlock();
          parent = curr;
          curr = (compare > 0)? curr.left : curr.right;
          parentCompare = compare;
        }
        if (curr == null) {
          break;
        } else curr.lock();
      }
    }
    parent.unlock();
    return false;
  }

  @Override
  public boolean contains(E element) {
    LockableNode<E> curr = null, parent = null;
    rootLock.lock();

    if (root == null) {
      rootLock.unlock();
      return false;
    } else {
      curr = root;
      curr.lock();
      rootLock.unlock();
      while (curr != null) {
        parent = curr;
        if (curr.element.compareTo(element) > 0) {
          curr = curr.left;
        } else if (curr.element.compareTo(element) < 0) {
          curr = curr.right;
        } else {
          curr.unlock();
          return true;
        }
      }
    }
    parent.unlock();
    return false;
  }

  private LockableNode<E> findMaxInLeftSubtree(LockableNode<E> subtree) {
    LockableNode<E> parent = subtree, curr = subtree.left;
    curr.lock();

    while (curr.right != null) {
      if (parent != subtree) {
        parent.unlock();
      }
      parent = curr;
      curr = curr.right;
      curr.lock();
    }

    if (curr.left != null) {
      curr.left.lock();
    }
    if (parent == subtree) {
      parent.left = curr.left;
    } else {
      parent.right = curr.left;
      parent.unlock();
    }
    if (curr.left != null) {
      curr.left.unlock();
    }
    curr.unlock();
    return curr;
  }

  private LockableNode<E> findMinInRightSubtree(LockableNode<E> subtree) {
    LockableNode<E> parent = subtree, curr = subtree.right;
    curr.lock();

    while (curr.left != null) {
      if (parent != subtree) {
        parent.unlock();
      }
      parent = curr;
      curr = curr.left;
      curr.lock();
    }

    if (curr.right != null) {
      curr.right.lock();
    }
    if (parent == subtree) {
      parent.right = curr.right;
    } else {
      parent.left = curr.right;
      parent.unlock();
    }
    if (curr.right != null) {
      curr.right.unlock();
    }
    curr.unlock();
    return curr;
  }

  private LockableNode<E> findReplacement(LockableNode<E> node) {
    if (node.left != null) {
      return findMaxInLeftSubtree(node);
    } else if (node.right != null) {
      return findMinInRightSubtree(node);
    } else return null; // leaf node
  }
}
