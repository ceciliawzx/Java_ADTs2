package ParallelBSTs.Sequential;

import ParallelBSTs.BST;

import java.util.*;
import java.util.stream.IntStream;

public class LinkedNodesBST<E extends Comparable<E>> implements BST<E> {

  private Node<E> root;

  public LinkedNodesBST(E element) {
    this.root = new Node<>(element);
  }

  public LinkedNodesBST() {
    this.root = null;
  }

  @Override
  public boolean add(E element) {
    if (root == null) {
      root = new Node<>(element);
      return true;
    } else return add(root, element);
  }

  // !!important, recursively use add.
  private boolean add(Node<E> subtree, E element) {
    // newNode already in;
    if (subtree.element.compareTo(element) == 0) {
      return false;
    } else if (subtree.element.compareTo(element) > 0) {
      if (subtree.left == null) {
        subtree.left = new Node<>(element);
        return true;
      } else return add(subtree.left, element);
    } else {
      if (subtree.right == null) {
        subtree.right = new Node<>(element);
        return true;
      } else return add(subtree.right, element);
    }
  }

  @Override
  public boolean remove(E element) {
    Set<Node<E>> deletedNodes = new HashSet<>();
    root = remove(root, element, deletedNodes);
    return !deletedNodes.isEmpty();
  }

  private Node<E> remove(Node<E> subtree, E element, Set<Node<E>> deletedNode) {
    if (subtree == null) {
      return null; // the target is not in it;
    } else if (subtree.element.compareTo(element) < 0) {
      subtree.right = remove(subtree.right, element, deletedNode);
    } else if (subtree.element.compareTo(element) > 0) {
      subtree.left = remove(subtree.left, element, deletedNode);
    } else {
      deletedNode.add(subtree);
      subtree = deleteNode(subtree);
    }
    return subtree;
  }

  private Node<E> deleteNode(Node<E> node) {
    if (node.left == null && node.right == null) {
      return null; // leaf
    } else if (node.left != null && node.right == null) {
      return node.left;
    } else if (node.left == null) {
      return node.right;
    } else {
      Node<E> newRoot = findMaxNode(node.left);
      newRoot.right = node.right;
      newRoot.left = removeMaxNode(node.left);
      return newRoot;
    }
  }

  private Node<E> findMinNode(Node<E> subtree) {
    if (subtree.left == null) {
      return subtree;
    } else return findMinNode(subtree.left);
  }

  private Node<E> findMaxNode(Node<E> subtree) {
    if (subtree.right == null) {
      return subtree;
    } else return findMaxNode(subtree.right);
  }

  private Node<E> removeMinNode(Node<E> subtree) {
    if (subtree.left == null) {
      return subtree.right;
    } else {
      subtree.left = removeMinNode(subtree.left);
      return subtree;
    }
  }

  private Node<E> removeMaxNode(Node<E> subtree) {
    if (subtree.right == null) {
      return subtree.left;
    } else {
      subtree.right = removeMaxNode(subtree.right);
      return subtree;
    }
  }

  @Override
  public boolean contains(E element) {
    return contains(root, element);
  }

  private boolean contains(Node<E> subtree, E element) {
    if (subtree == null) {
      return false;
    } else if (subtree.element.compareTo(element) == 0) {
      return true;
    } else if (subtree.element.compareTo(element) < 0) {
      return contains(subtree.right, element);
    } else return contains(subtree.left, element);
  }

  private class Node<T> {
    private T element;
    private Node<T> left;
    private Node<T> right;

    public Node(T element) {
      this.element = element;
    }
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    toString(root, 0, stringBuilder);
    return stringBuilder.toString() + '\n';
  }

  private void toString(Node<E> node, int indentation, StringBuilder stringBuilder) {
    if (node == null) {
      return;
    }
    IntStream.range(0, indentation).forEach(i -> stringBuilder.append("  "));
    stringBuilder.append(node.element + "\n");
    toString(node.left, indentation + 1, stringBuilder);
    toString(node.right, indentation + 1, stringBuilder);
  }

  public int height(Node<E> node) {
    if (node == null) {
      return 0;
    } else return 1 + Math.max(height(node.left), height(node.right));
  }

  public int height() {
    return height(root);
  }

  /* traversals */

  // Preorder: middle, left, right
  public void traversePreorder(Node<E> subtree, List<E> traversal) {
    if (subtree == null) {
      return;
    }
    traversal.add(subtree.element);
    traversePreorder(subtree.left, traversal);
    traversePreorder(subtree.right, traversal);
  }

  public List<E> traversePreorder() {
    List<E> traversal = new ArrayList<>();
    traversePreorder(root, traversal);
    return traversal;
  }

  // Inorder: left, middle, right
  public void traverseInorder(Node<E> subtree, List<E> traversal) {
    if (subtree == null) {
      return;
    }
    traverseInorder(subtree.left, traversal);
    traversal.add(subtree.element);
    traverseInorder(subtree.right, traversal);
  }

  public List<E> traverseInorder() {
    List<E> traversal = new ArrayList<>();
    traverseInorder(root, traversal);
    return traversal;
  }

  // Postorder: left, right, middle
  public void traversePostorder(Node<E> subtree, List<E> traversal) {
    if (subtree == null) {
      return;
    }
    traversePostorder(subtree.left, traversal);
    traversePostorder(subtree.right, traversal);
    traversal.add(subtree.element);
  }

  public List<E> traversePostorder() {
    List<E> traversal = new ArrayList<>();
    traversePostorder(root, traversal);
    return traversal;
  }

  // this method implements a post-order traversal using a stack
  // pre-order, in-order and post-order are variants of DFS
  public void DFS(Node<E> subtree, List<E> traversal) {
    if (subtree == null) {
      return;
    }
    Deque<Node<E>> stack = new ArrayDeque<>();
    Node<E> current = subtree;
    Node<E> lastNodeVisited = null;

    while (!stack.isEmpty() || current != null) {
      if (current != null) { // go in depth
        stack.push(current);
        current = current.left;
      } else {
        Node<E> peekNode = stack.peekFirst();
        if (peekNode.right != null && lastNodeVisited != peekNode.right) {
          current = peekNode.right;
        } else {
          traversal.add(peekNode.element);
          lastNodeVisited = stack.removeFirst();
        }
      }
    }
  }

  public List<E> DFS() {
    List<E> traversal = new ArrayList<>();
    DFS(root, traversal);
    return traversal;
  }

  // BFS: 将root连接的每个点加进队列的最后，再按顺序重复这个过程
  // Level by level
  public void BFS(Node<E> subtree, List<E> traversal) {
    Deque<Node<E>> queue = new ArrayDeque<>();
    queue.addLast(subtree);
    while (!queue.isEmpty()) {
      Node<E> node = queue.pollFirst();
      traversal.add(node.element);
      if (node.left != null) {
        queue.addLast(node.left);
      }
      if (node.right != null) {
        queue.addLast(node.right);
      }
    }
  }

  public List<E> BFS() {
    List<E> traversal = new ArrayList<>();
    BFS(root, traversal);
    return traversal;
  }

  public static void main(String[] args) {
    LinkedNodesBST<Integer> tree = new LinkedNodesBST<>();
    tree.add(5);
    tree.add(6);
    tree.add(4);
    tree.add(2);
    tree.add(3);
    tree.add(1);
    tree.add(8);
    tree.add(7);
    tree.add(10);
    tree.add(9);
    tree.add(11);
    System.out.println(tree);

    System.out.println(tree.traversePreorder());
    System.out.println(tree.traverseInorder());
    System.out.println(tree.traversePostorder());
    System.out.println(tree.BFS());
  }
}
