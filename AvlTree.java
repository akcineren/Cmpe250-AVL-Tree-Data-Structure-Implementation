import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.lang.model.util.ElementScanner14;
import javax.swing.text.html.parser.Element;
import javax.xml.crypto.Data;

import org.w3c.dom.Node;

public class AvlTree {

    class AvlNode {
        String name;
        float gmsValue;
        int height;
        AvlNode leftChild;
        AvlNode rightChild;
        AvlNode parent;

        public AvlNode() {
            this(null, -1, null, 0, null, null);
        }

        public AvlNode(String name, float gmsValue, AvlNode parent, int height, AvlNode leftChild, AvlNode rightChild) {
            this.name = name;
            this.gmsValue = gmsValue;
            this.parent = parent;
            this.height = height;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public AvlNode(String name, float gmsValue) {
            this(name, gmsValue, null, 0, null, null);
        }

        public AvlNode(String name, float gmsValue, AvlNode parent) {
            this(name, gmsValue, parent, 0, null, null);
        }

        public String getName() {
            return name;
        }

        public float getGmsValue() {
            return gmsValue;
        }

        public int getHeight() {
            return height;
        }

        public AvlNode getLeftChild() {
            return leftChild;
        }

        public AvlNode getRightChild() {
            return rightChild;
        }

        public AvlNode getParent() {
            return parent;
        }

    }

    AvlNode root;

    File output;
    FileWriter myWriter;

    public AvlTree(String outputName) throws IOException {
        this(null, outputName);

    }

    public AvlTree(AvlNode root, String outputName) throws IOException {
        output = new File(outputName);
        myWriter = new FileWriter(outputName);
        this.root = root;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    /***
     * Finds and returns the minimum node in the AVL tree.
     *
     * @return The minimum node in the tree.
     * @throws NoSuchElementException if the tree is empty.
     */
    public AvlNode findMinNode() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return findMinNode(this.root);
    }

    /**
     * Gets the height of an AVL node. The height of a null node is considered to be
     * -1,
     * while the height of a valid AVL node is the value stored in its 'height'
     * attribute.
     *
     * @param node The AVL node for which the height is to be determined.
     * @return The height of the AVL node, or -1 if the node is null.
     */
    private int avlNodeHeight(AvlNode node) {
        if (node == null) {
            return -1;
        }
        return node.height;
    }

    /**
     * Finds and returns the minimum node in a given subtree rooted at the specified
     * node.
     * The minimum node is found by traversing left child nodes until the leftmost
     * node is reached.
     *
     * @param node The root node of the subtree in which to find the minimum node.
     * @return The minimum node in the specified subtree.
     */
    private AvlNode findMinNode(AvlNode node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }

    /**
     * Finds and returns the maximum node in the AVL tree.
     *
     * @return The maximum node in the tree.
     * @throws NoSuchElementException if the tree is empty.
     */
    public AvlNode findMaxNode() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return findMaxNode(this.root);
    }

    /**
     * Finds and returns the maximum node in a given subtree rooted at the specified
     * node.
     * The maximum node is found by traversing right child nodes until the rightmost
     * node is reached.
     *
     * @param node The root node of the subtree in which to find the maximum node.
     * @return The maximum node in the specified subtree.
     */
    private AvlNode findMaxNode(AvlNode node) {
        while (node.rightChild != null) {
            node = node.rightChild;
        }
        return node;
    }

    public void insert(String name, float gmsValue) throws IOException {
        this.root = this.insert(name, gmsValue, this.root, null);

    }

    /**
     * Inserts a new node with the specified name and grams value into the AVL tree
     * rooted at the given node.
     * If a node with the same grams value already exists in the tree, it will not
     * be inserted.
     *
     * @param name     The name associated with the grams value to be inserted.
     * @param gmsValue The grams value to be inserted.
     * @param node     The current node in the tree where insertion is being
     *                 performed.
     * @param parent   The parent node of the current node (null if the current node
     *                 is the root).
     * @return The root of the updated AVL tree after insertion.
     * @throws IOException if there's an issue writing to an external resource
     *                     (e.g., a file) for logging.
     */
    private AvlNode insert(String name, float gmsValue, AvlNode node, AvlNode parent) throws IOException {
        if (node == null) {
            return new AvlNode(name, gmsValue, parent);
        }
        if (gmsValue < node.getGmsValue()) {

            myWriter.write(node.name + " welcomed " + name + "\n");
            node.leftChild = insert(name, gmsValue, node.leftChild, node);

            if (avlNodeHeight(node.leftChild) - avlNodeHeight(node.rightChild) > 1) {
                if (gmsValue < node.leftChild.gmsValue) {
                    node = srWithLeftChild(node);
                } else {
                    node = drWithLeftChild(node);
                }
            }
        } else if (gmsValue > node.getGmsValue()) {
            myWriter.write(node.name + " welcomed " + name + "\n");
            node.rightChild = insert(name, gmsValue, node.rightChild, node);

            if (avlNodeHeight(node.rightChild) - avlNodeHeight(node.leftChild) > 1) {
                if (gmsValue > node.rightChild.gmsValue) {
                    node = srWithRightChild(node);
                } else {
                    node = drWithRightChild(node);
                }
            }
        }

        node.height = Math.max(avlNodeHeight(node.leftChild), avlNodeHeight(node.rightChild)) + 1;
        return node;
    }

    /**
     * Performs a single right rotation (SR) on the given AVL node to fix an
     * imbalance.
     *
     * @param node The AVL node where the single right rotation is performed.
     * @return The new root node after the rotation.
     */
    private AvlNode srWithLeftChild(AvlNode node) {
        AvlNode tempNode = node.leftChild;
        node.leftChild = tempNode.rightChild;
        tempNode.rightChild = node;
        node.height = Math.max(avlNodeHeight(node.leftChild), avlNodeHeight(node.rightChild)) + 1;
        tempNode.height = Math.max(avlNodeHeight(tempNode.leftChild), avlNodeHeight(tempNode.rightChild)) + 1;
        return tempNode;
    }

    /**
     * Performs a single left rotation (SR) on the given AVL node to fix an
     * imbalance.
     *
     * @param node The AVL node where the single left rotation is performed.
     * @return The new root node after the rotation.
     */
    private AvlNode srWithRightChild(AvlNode node) {
        AvlNode tempNode = node.rightChild;
        node.rightChild = tempNode.leftChild;
        tempNode.leftChild = node;
        node.height = Math.max(avlNodeHeight(node.leftChild), avlNodeHeight(node.rightChild)) + 1;
        tempNode.height = Math.max(avlNodeHeight(tempNode.leftChild), avlNodeHeight(tempNode.rightChild)) + 1;
        return tempNode;
    }

    /**
     * Performs a double right-left rotation (DR) on the given AVL node to fix an
     * imbalance.
     * This operation involves performing a single right rotation on the left child
     * of the node
     * followed by a single left rotation on the node itself.
     *
     * @param node The AVL node where the double right-left rotation is performed.
     * @return The new root node after the double right-left rotation.
     */
    private AvlNode drWithLeftChild(AvlNode node) {
        node.leftChild = srWithRightChild(node.leftChild);
        return srWithLeftChild(node);
    }

    /**
     * Performs a double left-right rotation (DR) on the given AVL node to fix an
     * imbalance.
     * This operation involves performing a single left rotation on the right child
     * of the node
     * followed by a single right rotation on the node itself.
     *
     * @param node The AVL node where the double left-right rotation is performed.
     * @return The new root node after the double left-right rotation.
     */
    private AvlNode drWithRightChild(AvlNode node) {
        node.rightChild = srWithLeftChild(node.rightChild);
        return srWithRightChild(node);
    }

    /**
     * Searches for a node with the given name and grams value in the AVL tree and
     * returns its rank.
     * The rank represents the position of the node based on its grams value, with 0
     * being the highest.
     *
     * @param name     The name of the node to search for.
     * @param gmsValue The grams value of the node to search for.
     * @return The rank of the node, or -1 if the node is not found in the tree.
     */
    public int searchNodeRank(String name, float gmsValue) {
        AvlNode current = root;
        int i = 0;
        while (true) {
            if (current == null) {
                break;
            } else if (gmsValue < current.gmsValue) {
                current = current.leftChild;
            } else if (gmsValue > current.gmsValue) {
                current = current.rightChild;
            } else {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Calculates the balance factor of an AVL node, which is the difference between
     * the height
     * of the left subtree and the height of the right subtree.
     *
     * @param node The AVL node for which to calculate the balance factor.
     * @return The balance factor of the node (positive if left-heavy, negative if
     *         right-heavy, 0 if balanced).
     */
    private int calculateBalance(AvlNode node) {
        if (node == null) {
            return 0;
        }
        return avlNodeHeight(node.leftChild) - avlNodeHeight(node.rightChild);

    }

    public void delete(String name, float gmsValue) throws IOException {
        root = delete(root, new AvlNode(name, gmsValue), true);
    }

    /**
     * Deletes a node with the specified name and grams value from the AVL tree, if
     * it exists, and maintains
     * the balance of the tree. The method may also perform rotations to maintain
     * the AVL tree's properties.
     *
     * @param root  The current root of the subtree where deletion is being
     *              performed.
     * @param item  The AVL node to be deleted.
     * @param first A flag indicating if this is the initial call in the deletion
     *              process.
     * @return The updated root of the subtree after deletion and balancing.
     * @throws IOException if there's an issue writing to an external resource
     *                     (e.g., a file) for logging.
     */
    private AvlNode delete(AvlNode root, AvlNode item, boolean first) throws IOException {
        if (root == null) {
            return root;
        }

        if (item.gmsValue < root.gmsValue) {
            root.leftChild = delete(root.leftChild, item, first);
        }

        else if (item.gmsValue > root.gmsValue) {
            root.rightChild = delete(root.rightChild, item, first);
        }

        else {
            if (root.leftChild == null && root.rightChild != null) {
                if (first) {
                    myWriter.write(root.name + " left the family, replaced by " + root.rightChild.name + "\n");

                }
                root = root.rightChild;
            } else if (root.leftChild != null && root.rightChild == null) {
                if (first) {
                    myWriter.write(root.name + " left the family, replaced by " + root.leftChild.name + "\n");

                }
                root = root.leftChild;
            } else if (root.leftChild == null && root.rightChild == null) {
                if (first) {
                    myWriter.write(root.name + " left the family, replaced by nobody" + "\n");

                }
                root = null;
            }

            else if (root.rightChild != null && root.leftChild != null) {

                AvlNode tempNode = findMinNode(root.rightChild);
                if (first) {
                    myWriter.write(root.name + " left the family, replaced by " + tempNode.name + "\n");

                    first = false;
                }
                root.name = tempNode.name;
                root.gmsValue = tempNode.gmsValue;
                root.rightChild = delete(root.rightChild, tempNode, first);
            }
        }

        if (root == null) {
            return root;
        }

        root.height = Math.max(avlNodeHeight(root.leftChild), avlNodeHeight(root.rightChild)) + 1;

        int balanceNum = calculateBalance(root);

        if (balanceNum > 1 && calculateBalance(root.leftChild) >= 0) {
            return srWithLeftChild(root);
        }

        if (balanceNum > 1 && calculateBalance(root.leftChild) < 0) {
            root.leftChild = srWithRightChild(root.leftChild);
            return srWithLeftChild(root);
        }

        if (balanceNum < -1 && calculateBalance(root.rightChild) <= 0) {
            return srWithRightChild(root);
        }

        if (balanceNum < -1 && calculateBalance(root.rightChild) > 0) {
            root.rightChild = srWithLeftChild(root.rightChild);
            return srWithRightChild(root);
        }

        return root;
    }

    public int intel_divide() throws IOException {
        int[] res = intel_divide(root);
        myWriter.write("Division Analysis Result: " + Math.max(res[0], res[1]) + "\n");
        return Math.max(res[0], res[1]);
    }

    private int getMaxOfList(int[] list) {
        return list[0] >= list[1] ? list[0] : list[1];
    }

    /**
     * Recursively computes two values for a given AVL node in the context of a tree
     * division problem:
     * - The maximum value achievable with the current node included.
     * - The maximum value achievable without the current node included.
     *
     * @param node The AVL node for which to compute the two values.
     * @return An array containing two integers: [withRoot, withoutRoot]
     *         - withRoot: The maximum value achievable with the current node
     *         included.
     *         - withoutRoot: The maximum value achievable without the current node
     *         included.
     */
    private int[] intel_divide(AvlNode node) {

        if (node == null) {
            return new int[] { 0, 0 };
        }

        int[] leftChild = intel_divide(node.leftChild);
        int[] rightChild = intel_divide(node.rightChild);
        int withRoot = leftChild[1] + rightChild[1] + 1;
        int withoutRoot = getMaxOfList(leftChild) + getMaxOfList(rightChild);
        int[] returnLst = new int[2];
        returnLst[0] = withRoot;
        returnLst[1] = withoutRoot;
        return returnLst;

    }

    /**
     * Recursively computes two values for a given AVL node in the context of a tree
     * division problem:
     * - The maximum value achievable with the current node included.
     * - The maximum value achievable without the current node included.
     *
     * @param node The AVL node for which to compute the two values.
     * @return An array containing two integers: [withRoot, withoutRoot]
     *         - withRoot: The maximum value achievable with the current node
     *         included.
     *         - withoutRoot: The maximum value achievable without the current node
     *         included.
     */
    public void intel_targeter(String name1, String name2, float gmsValue1, float gmsValue2) throws IOException {
        AvlNode current = root;
        while (true) {
            if (gmsValue1 < current.gmsValue && gmsValue2 < current.gmsValue) {
                current = current.leftChild;
            } else if (gmsValue1 > current.gmsValue && gmsValue2 > current.gmsValue) {
                current = current.rightChild;
            } else {
                myWriter.write("Target Analysis Result: " + current.name + " "
                        + String.format("%.3f", current.gmsValue).replace(",", ".") + "\n");
                // System.out.println("Target Analysis Result: " + current.name + " ");
                break;
            }
        }
    }

    /**
     * Performs a rank analysis in the AVL tree and writes the result to an external
     * resource.
     * The method finds nodes with the same rank as the node with the specified name
     * and grams value
     * and writes their names and grams values to the output.
     *
     * @param name     The name for which the rank analysis is performed.
     * @param gmsValue The grams value for which the rank analysis is performed.
     * @throws IOException if there's an issue writing to an external resource
     *                     (e.g., a file) for logging.
     */
    public void intel_rank(String name, float gmsValue) throws IOException {
        int rank = 0;
        int des_rank = searchNodeRank(name, gmsValue);

        Queue<AvlNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int relRanks = queue.size();
            if (rank == des_rank) {
                myWriter.write("Rank Analysis Result:");
                // System.out.print("Rank Analysis Result: ");
                while (!queue.isEmpty()) {
                    AvlNode current = queue.poll();
                    myWriter.write(" " + current.name + " ");
                    myWriter.write(String.format("%.3f", current.gmsValue).replace(",", "."));

                }
                myWriter.write("\n");
                break;

            } else {
                for (int i = 0; i < relRanks; i++) {
                    AvlNode current = queue.poll();
                    if (current.leftChild != null) {
                        queue.add(current.leftChild);
                    }
                    if (current.rightChild != null) {
                        queue.add(current.rightChild);
                    }
                }
                rank++;
            }
        }

    }

}
