import java.util.*;
import java.util.function.Consumer;

// Задание 1: Реализация бинарного дерева
public class BinaryTree<E> implements AbstractBinaryTree<E> {
    private E key;
    BinaryTree<E> left;
    BinaryTree<E> right;

    public BinaryTree(E key) {
        this.key = key;
    }

    @Override
    public E getKey() {
        return key;
    }

    @Override
    public AbstractBinaryTree<E> getLeft() {
        return left;
    }

    @Override
    public AbstractBinaryTree<E> getRight() {
        return right;
    }

    @Override
    public void setKey(E key) {
        this.key = key;
    }

    @Override
    public String asIndentedPreOrder(int indent) {
        StringJoiner result = new StringJoiner("\n");
        String currentIndent = "  ".repeat(indent);
        result.add(currentIndent + key.toString());
        if (left != null) {
            result.add(left.asIndentedPreOrder(indent + 1));
        }
        if (right != null) {
            result.add(right.asIndentedPreOrder(indent + 1));
        }
        return result.toString();
    }

    @Override
    public List<AbstractBinaryTree<E>> preOrder() {
        List<AbstractBinaryTree<E>> result = new ArrayList<>();
        result.add(this);
        if (left != null) {
            result.addAll(left.preOrder());
        }
        if (right != null) {
            result.addAll(right.preOrder());
        }
        return result;
    }

    @Override
    public List<AbstractBinaryTree<E>> inOrder() {
        List<AbstractBinaryTree<E>> result = new ArrayList<>();
        if (left != null) {
            result.addAll(left.inOrder());
        }
        result.add(this);
        if (right != null) {
            result.addAll(right.inOrder());
        }
        return result;
    }

    @Override
    public List<AbstractBinaryTree<E>> postOrder() {
        List<AbstractBinaryTree<E>> result = new ArrayList<>();
        if (left != null) {
            result.addAll(left.postOrder());
        }
        if (right != null) {
            result.addAll(right.postOrder());
        }
        result.add(this);
        return result;
    }

    @Override
    public void forEachInOrder(Consumer<E> consumer) {
        if (left != null) {
            left.forEachInOrder(consumer);
        }
        consumer.accept(key);
        if (right != null) {
            right.forEachInOrder(consumer);
        }
    }

    @Override
    public String asIndentedPostOrder(int indent) {
        StringJoiner result = new StringJoiner("\n");
        String currentIndent = "  ".repeat(indent);
        if (right != null) {
            result.add(right.asIndentedPostOrder(indent + 1));
        }
        result.add(currentIndent + key.toString());
        if (left != null) {
            result.add(left.asIndentedPostOrder(indent + 1));
        }
        return result.toString();
    }


    @Override
    public String levelOrder() {
        StringBuilder result = new StringBuilder();
        if (this != null) {
            Queue<AbstractBinaryTree<E>> queue = new LinkedList<>();
            queue.add(this);

            while (!queue.isEmpty()) {
                AbstractBinaryTree<E> current = queue.poll();
                result.append(current.getKey()).append(" ");

                if (current.getLeft() != null) {
                    queue.add(current.getLeft());
                }
                if (current.getRight() != null) {
                    queue.add(current.getRight());
                }
            }
        }
        return result.toString().trim();
    }

}

// Задание 2: Расширение контракта для обхода в обратном порядке и в ширину
interface AbstractBinaryTree<E> {
    E getKey();
    AbstractBinaryTree<E> getLeft();
    AbstractBinaryTree<E> getRight();
    void setKey(E key);
    String asIndentedPreOrder(int indent);
    List<AbstractBinaryTree<E>> preOrder();
    List<AbstractBinaryTree<E>> inOrder();
    List<AbstractBinaryTree<E>> postOrder();
    void forEachInOrder(Consumer<E> consumer);
    String asIndentedPostOrder(int indent);
    String levelOrder();
}

// Задание 3: Реализация бинарного дерева поиска
class BinarySearchTree<E extends Comparable<E>> implements AbstractBinarySearchTree<E> {
    private Node<E> root;

    public static class Node<E> {
        public E value;
        public Node<E> leftChild;
        public Node<E> rightChild;

        public Node(E value) {
            this.value = value;
        }

        public Node(E value, Node<E> leftChild, Node<E> rightChild) {
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }
    }

    @Override
    public void insert(E element) {
        root = insertRecursive(root, element);
    }

    private Node<E> insertRecursive(Node<E> node, E element) {
        if (node == null) {
            return new Node<>(element);
        }

        int compareResult = element.compareTo(node.value);
        if (compareResult < 0) {
            node.leftChild = insertRecursive(node.leftChild, element);
        } else if (compareResult > 0) {
            node.rightChild = insertRecursive(node.rightChild, element);
        }

        return node;
    }

    @Override
    public boolean contains(E element) {
        return containsRecursive(root, element);
    }

    private boolean containsRecursive(Node<E> node, E element) {
        if (node == null) {
            return false;
        }

        int compareResult = element.compareTo(node.value);
        if (compareResult < 0) {
            return containsRecursive(node.leftChild, element);
        } else if (compareResult > 0) {
            return containsRecursive(node.rightChild, element);
        } else {
            return true;
        }
    }

    @Override
    public AbstractBinarySearchTree<E> search(E element) {
        Node<E> resultNode = searchRecursive(root, element);
        return resultNode != null ? createSearchTreeFromNode(resultNode) : new BinarySearchTree<>();
    }

    private Node<E> searchRecursive(Node<E> node, E element) {
        if (node == null || element.equals(node.value)) {
            return node;
        }

        int compareResult = element.compareTo(node.value);
        if (compareResult < 0) {
            return searchRecursive(node.leftChild, element);
        } else {
            return searchRecursive(node.rightChild, element);
        }
    }

    private BinarySearchTree<E> createSearchTreeFromNode(Node<E> node) {
        BinarySearchTree<E> resultTree = new BinarySearchTree<>();
        resultTree.root = node;
        return resultTree;
    }

    @Override
    public Node<E> getRoot() {
        return root;
    }

    @Override
    public Node<E> getLeft() {
        return root != null ? root.leftChild : null;
    }

    @Override
    public Node<E> getRight() {
        return root != null ? root.rightChild : null;
    }

    @Override
    public E getValue() {
        return root != null ? root.value : null;
    }

    @Override
    public String asIndentedPreOrder(int indent) {
        return asIndentedPreOrder(root, indent);
    }

    private String asIndentedPreOrder(Node<E> node, int indent) {
        if (node == null) {
            return "";
        }
        StringJoiner result = new StringJoiner("\n");
        String currentIndent = "  ".repeat(indent);
        result.add(currentIndent + node.value.toString());
        result.add(asIndentedPreOrder(node.leftChild, indent + 1));
        result.add(asIndentedPreOrder(node.rightChild, indent + 1));
        return result.toString();
    }

    @Override
    public String levelOrder() {
        StringBuilder result = new StringBuilder();
        if (root != null) {
            Queue<Node<E>> queue = new LinkedList<>();
            queue.add(root);

            while (!queue.isEmpty()) {
                Node<E> current = queue.poll();
                result.append(current.value).append(" ");

                if (current.leftChild != null) {
                    queue.add(current.leftChild);
                }
                if (current.rightChild != null) {
                    queue.add(current.rightChild);
                }
            }
        }
        return result.toString().trim();
    }
}

// Задание 4: Процедура COPY для копирования дерева
class TreeCopy {
    public static <E> AbstractBinaryTree<E> copyTree(AbstractBinaryTree<E> tree) {
        if (tree == null) {
            return null;
        }
        BinaryTree<E> copiedTree = new BinaryTree<>(tree.getKey());
        copiedTree.left = (BinaryTree<E>) copyTree(tree.getLeft());
        copiedTree.right = (BinaryTree<E>) copyTree(tree.getRight());
        return copiedTree;
    }
}


class Main {
    public static void main(String[] args) {
        // Задание 1: Реализация бинарного дерева
        BinaryTree<Integer> binaryTree = new BinaryTree<>(10);
        binaryTree.left = new BinaryTree<>(5);
        binaryTree.right = new BinaryTree<>(15);
        binaryTree.left.left = new BinaryTree<>(3);
        binaryTree.left.right = new BinaryTree<>(7);
        binaryTree.right.right = new BinaryTree<>(20);

        System.out.println("Задание 1: Реализация бинарного дерева");
        System.out.println("Прямой обход:");
        for (AbstractBinaryTree<Integer> node : binaryTree.preOrder()) {
            System.out.print(node.getKey() + " ");
        }

        System.out.println("\nСимметричный обход:");
        for (AbstractBinaryTree<Integer> node : binaryTree.inOrder()) {
            System.out.print(node.getKey() + " ");
        }

        System.out.println("\nОбратный обход:");
        for (AbstractBinaryTree<Integer> node : binaryTree.postOrder()) {
            System.out.print(node.getKey() + " ");
        }

        System.out.println("\nОбход в ширину:");
        System.out.println(binaryTree.levelOrder());

        System.out.println("\nВывод дерева в виде дерева:");
        System.out.println(binaryTree.asIndentedPreOrder(0));

        // Задание 2: Расширение контракта для обхода в обратном порядке и в ширину
        System.out.println("\n\nЗадание 2: Расширение контракта");
        System.out.println("Обратный обход с отступами:");
        System.out.println(binaryTree.asIndentedPostOrder(0));

        // Задание 3: Реализация бинарного дерева поиска
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        int[] elements = {10, 5, 15, 3, 7, 20};

        for (int element : elements) {
            binarySearchTree.insert(element);
        }

        System.out.println("\n\nЗадание 3: Реализация бинарного дерева поиска");
        System.out.println("Дерево поиска:");
        System.out.println(binarySearchTree.asIndentedPreOrder(0));

        // Задание 4: Процедура COPY для копирования дерева
        System.out.println("\n\nЗадание 4: Процедура COPY");
        AbstractBinaryTree<Integer> copiedTree = TreeCopy.copyTree(binaryTree);
        System.out.println("Скопированное дерево:");
        System.out.println(copiedTree.asIndentedPreOrder(0));
    }
}
