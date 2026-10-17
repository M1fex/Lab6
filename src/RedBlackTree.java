public class RedBlackTree {
    private Node root;

    public boolean insert(int value) {
        if (root == null) {
            root = new Node(value);
            root.isRed = false;
            return true;
        }

        Node current = root;
        Node parent = null;
        while (current != null) {
            parent = current;
            if (value == current.value) return false;
            current = value < current.value ? current.left : current.right;
        }

        Node node = new Node(value);
        node.isRed = true;
        node.parent = parent;
        if (value < parent.value) parent.left = node;
        else parent.right = node;
        fixInsert(node);
        return true;
    }

    private void rotateLeft(Node node) {
        if (node == null || node.right == null) return;
        Node pivot = node.right;
        node.right = pivot.left;
        if (pivot.left != null) pivot.left.parent = node;
        pivot.parent = node.parent;
        if (node.parent == null) root = pivot;
        else if (node.parent.left == node) node.parent.left = pivot;
        else node.parent.right = pivot;
        pivot.left = node;
        node.parent = pivot;
    }

    private void rotateRight(Node node) {
        if (node == null || node.left == null) return;
        Node pivot = node.left;
        node.left = pivot.right;
        if (pivot.right != null) pivot.right.parent = node;
        pivot.parent = node.parent;
        if (node.parent == null) root = pivot;
        else if (node.parent.left == node) node.parent.left = pivot;
        else node.parent.right = pivot;
        pivot.right = node;
        node.parent = pivot;
    }

    private void fixInsert(Node node) {
        while (node != root && node.parent.isRed) {
            Node parent = node.parent;
            Node grandparent = parent.parent;
            Node uncle = parent == grandparent.left ? grandparent.right : grandparent.left;

            if (isRed(uncle)) {
                parent.isRed = false;
                uncle.isRed = false;
                grandparent.isRed = true;
                node = grandparent;
            } else if (parent == grandparent.left) {
                if (node == parent.right) {
                    rotateLeft(parent);
                    parent = node;
                }
                rotateRight(grandparent);
                parent.isRed = false;
                grandparent.isRed = true;
                break;
            } else {
                if (node == parent.left) {
                    rotateRight(parent);
                    parent = node;
                }
                rotateLeft(grandparent);
                parent.isRed = false;
                grandparent.isRed = true;
                break;
            }
        }
        root.isRed = false;
    }

    public boolean delete(int value) {
        Node node = find(value);
        if (node == null) return false;

        Node replacement = node;
        boolean wasRed = replacement.isRed;
        Node child;
        Node childParent;

        if (node.left == null) {
            child = node.right;
            childParent = node.parent;
            transplant(node, node.right);
        } else if (node.right == null) {
            child = node.left;
            childParent = node.parent;
            transplant(node, node.left);
        } else {
            replacement = minimum(node.right);
            wasRed = replacement.isRed;
            child = replacement.right;
            if (replacement.parent == node) {
                childParent = replacement;
            } else {
                childParent = replacement.parent;
                transplant(replacement, replacement.right);
                replacement.right = node.right;
                replacement.right.parent = replacement;
            }
            transplant(node, replacement);
            replacement.left = node.left;
            replacement.left.parent = replacement;
            replacement.isRed = node.isRed;
        }

        if (!wasRed) fixDelete(child, childParent);
        return true;
    }

    private void fixDelete(Node node, Node parent) {
        while (node != root && !isRed(node)) {
            if (parent == null) break;
            if (node == parent.left) {
                Node sibling = parent.right;
                if (isRed(sibling)) {
                    sibling.isRed = false;
                    parent.isRed = true;
                    rotateLeft(parent);
                    sibling = parent.right;
                }
                if (!isRed(leftOf(sibling)) && !isRed(rightOf(sibling))) {
                    if (sibling != null) sibling.isRed = true;
                    node = parent;
                    parent = node.parent;
                } else {
                    if (!isRed(rightOf(sibling))) {
                        if (leftOf(sibling) != null) leftOf(sibling).isRed = false;
                        sibling.isRed = true;
                        rotateRight(sibling);
                        sibling = parent.right;
                    }
                    sibling.isRed = parent.isRed;
                    parent.isRed = false;
                    if (rightOf(sibling) != null) rightOf(sibling).isRed = false;
                    rotateLeft(parent);
                    node = root;
                }
            } else {
                Node sibling = parent.left;
                if (isRed(sibling)) {
                    sibling.isRed = false;
                    parent.isRed = true;
                    rotateRight(parent);
                    sibling = parent.left;
                }
                if (!isRed(leftOf(sibling)) && !isRed(rightOf(sibling))) {
                    if (sibling != null) sibling.isRed = true;
                    node = parent;
                    parent = node.parent;
                } else {
                    if (!isRed(leftOf(sibling))) {
                        if (rightOf(sibling) != null) rightOf(sibling).isRed = false;
                        sibling.isRed = true;
                        rotateLeft(sibling);
                        sibling = parent.left;
                    }
                    sibling.isRed = parent.isRed;
                    parent.isRed = false;
                    if (leftOf(sibling) != null) leftOf(sibling).isRed = false;
                    rotateRight(parent);
                    node = root;
                }
            }
        }
        if (node != null) node.isRed = false;
    }

    private void transplant(Node oldNode, Node newNode) {
        if (oldNode.parent == null) root = newNode;
        else if (oldNode == oldNode.parent.left) oldNode.parent.left = newNode;
        else oldNode.parent.right = newNode;
        if (newNode != null) newNode.parent = oldNode.parent;
    }

    private Node find(int value) {
        Node current = root;
        while (current != null) {
            if (value == current.value) return current;
            current = value < current.value ? current.left : current.right;
        }
        return null;
    }

    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private static boolean isRed(Node node) {
        return node != null && node.isRed;
    }

    private static Node leftOf(Node node) {
        return node == null ? null : node.left;
    }

    private static Node rightOf(Node node) {
        return node == null ? null : node.right;
    }

    public void inOrder() {
        if (root == null) {
            System.out.println("Дерево порожнє.");
            return;
        }
        inOrder(root);
        System.out.println();
    }

    private void inOrder(Node node) {
        if (node == null) return;
        inOrder(node.left);
        System.out.print(node.value + (node.isRed ? "(R) " : "(B) "));
        inOrder(node.right);
    }

    public void printTree() {
        if (root == null) {
            System.out.println("Дерево порожнє.");
            return;
        }
        System.out.println(root.value + color(root));
        printBranch(root.left, "", root.right == null, "L");
        printBranch(root.right, "", true, "R");
    }

    private void printBranch(Node node, String prefix, boolean last, String side) {
        if (node == null) return;
        System.out.println(prefix + (last ? "└── " : "├── ")
                + side + ": " + node.value + color(node));
        String next = prefix + (last ? "    " : "│   ");
        printBranch(node.left, next, node.right == null, "L");
        printBranch(node.right, next, true, "R");
    }

    private String color(Node node) {
        return node.isRed ? "(R)" : "(B)";
    }

    public boolean isValid() {
        return (root == null || (!root.isRed && root.parent == null))
                && check(root, null, Long.MIN_VALUE, Long.MAX_VALUE) >= 0;
    }

    private int check(Node node, Node expectedParent, long min, long max) {
        if (node == null) return 1;
        if (node.parent != expectedParent || node.value <= min || node.value >= max)
            return -1;
        if (node.isRed && (isRed(node.left) || isRed(node.right))) return -1;
        int leftHeight = check(node.left, node, min, node.value);
        int rightHeight = check(node.right, node, node.value, max);
        if (leftHeight < 0 || leftHeight != rightHeight) return -1;
        return leftHeight + (node.isRed ? 0 : 1);
    }
}


class Node {
    boolean isRed;
    Node left;
    Node right;
    Node parent;
    int value;

    public Node(int value) {
        this.value = value;
    }
}