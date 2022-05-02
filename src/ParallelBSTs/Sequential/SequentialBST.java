package ParallelBSTs.Sequential;

import ParallelBSTs.BST;

public class SequentialBST<E extends Comparable<E>> implements BST<E> {

  protected Node<E> root;

  public SequentialBST(E element) {
    this.root = new Node<>(element);
  }

  public SequentialBST() {
    this.root = null;
  }

  @Override
  public boolean add(E element) {
    if (root == null) {
      root = new Node<>(element);
      return true;
    }

    Node<E> curr = root, parent = null, newNode = new Node<>(element);

    while (curr != null) {
      parent = curr;
      if (curr.element.compareTo(element) == 0) {
        return false;
      } else if (curr.element.compareTo(element) > 0) {
        curr = curr.left;
      } else curr = curr.right;
    }

    if (parent.element.compareTo(element) > 0) {
      parent.left = newNode;
    } else parent.right = newNode;
    return true;
  }

  @Override
  public boolean remove(E element) {
    Node<E> curr = root;
    Node<E> parent = null;
    while (curr != null && curr.element.compareTo(element) != 0) {
      parent = curr;
      if (curr.element.compareTo(element) > 0) {
        curr = curr.left;
      } else curr = curr.right;
    }
    // the target node is not in the tree;
    if (curr == null) {
      return false;
    }
    if (parent == null) {
      // target node is root;
      root = deleteNode(root);
    } else if (parent.element.compareTo(element) > 0) {
      // target node is on the left of parent, delete left node;
      parent.left = deleteNode(parent.left);
    } else parent.right = deleteNode(parent.right); // target node is on the right of parent.
    return true;
  }

  @Override
  public boolean contains(E element) {
    Node<E> curr = root;
    while (curr != null) {
      if (curr.element.compareTo(element) == 0) {
        return true;
      } else if (curr.element.compareTo(element) > 0) {
        curr = curr.left;
      } else curr = curr.right;
    }
    return false;
  }

  private Node<E> deleteNode(Node<E> node) {
    if (root.right != null) {
      // select the min node in the right subtree of the target node;
      Node<E> newRoot = findMinNode(node.right);
      // the new right subtree of the new root should be the original right subtree without new
      // root;
      newRoot.right = removeMinNode(node.right);
      // the new left subtree of the new root should be the original left subtree;
      newRoot.left = node.left;
      return newRoot;
    } else if (root.left != null) {
      Node<E> newRoot = findMaxNode(node.left);
      newRoot.left = removeMaxNode(node.left);
      newRoot.right = node.right;
      return newRoot;
    }
    // if the target node is a leaf;
    return null;
  }

  private Node<E> findMinNode(Node<E> subtree) {
    Node<E> curr = subtree;
    while (curr.left != null) {
      curr = curr.left;
    }
    return curr;
  }

  private Node<E> findMaxNode(Node<E> subtree) {
    Node<E> curr = subtree;
    while (curr.right != null) {
      curr = curr.right;
    }
    return curr;
  }

  private Node<E> removeMinNode(Node<E> subtree) {
    assert subtree != null;
    Node<E> curr = subtree;
    Node<E> parent = null;
    while (curr.left != null) {
      parent = curr;
      curr = curr.left;
    }
    if (parent == null) {
      return curr.right;
    } else {
      parent.left = curr.right;
      return subtree;
    }
  }

  private Node<E> removeMaxNode(Node<E> subtree) {
    assert subtree != null;
    Node<E> curr = subtree;
    Node<E> parent = null;
    while (curr.right != null) {
      parent = curr;
      curr = curr.right;
    }
    if (parent == null) {
      return curr.left;
    } else {
      parent.right = curr.left;
      return subtree;
    }
  }

  private static class Node<T> {
    private T element;
    private Node<T> left;
    private Node<T> right;

    public Node(T element) {
      this.element = element;
    }

    @Override
    public String toString() {
      return "N[" + element + "]";
    }
  }
}
