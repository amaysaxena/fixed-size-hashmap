/**
 * Created by amaysaxena on 30/09/2017.
 */
/**
 * A custom implementation of a left-leaning red-black tree.
 * The backing array of the hashmap may use an LLRB as its container as opposed
 * to the traditional linked list. This allows for worst case O(log N) operations
 * as opposed to worst case linear operations.
 */
public class BTreeNode<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    Pair _root;
    BTreeNode _left;
    BTreeNode _right;
    boolean _color;

    /**
     * A private class to store (key, value) associations.
     */
    private class Pair {
        private String _key;
        private T _val;

        Pair(String k, T v) {
            _key = k;
            _val = v;
        }

        String key() {
            return _key;
        }

        T value() {
            return _val;
        }

        boolean setValue(T val) {
            _val = val;
            return true;
        }
    }

    BTreeNode(Pair r, BTreeNode left, BTreeNode right, boolean color) {
        _root = r;
        _left = left;
        _right = right;
        _color = color;
    }

    // is node x red; false if x is null
    private boolean isRed(BTreeNode x) {
        return x != null && x._color == RED;
    }

    /**
     * Returns the Value associated with @param key in the BTree with this as the root.
     *
     * @param key The key for which an associated value is to be found.
     * @return The value associated with key.
     */
    private T get(String key) {
        BTreeNode b = this;
        while (b != null) {
            if (b._root.key().equals(key)) return _root.value();
            if (b._root.key().compareTo(key) > 0) b = b._right;
            else b = b._left;
        }
        return null;
    }

    /**
     * Remove and return the value associated with this key.
     *
     * @param key the query key.
     * @return the value associated with the key.
     */
    private void remove(BTreeNode b, String key) {
        if (key.compareTo(b._root.key()) < 0) {
            if (b._left == null) return;

            if (!isRed(b._left) && !isRed(b._left._left)) {
                moveRedLeft(b);
            }
        }

    }

    /**
     * Return the node with the minimum key value in the subtree starting at
     * node b
     *
     * @param b the node from which the search is being initiated.
     * @return the node with the minimum key in this subtree.
     */
    private BTreeNode min(BTreeNode b) {
        BTreeNode x = b;
        if (x._left == null) return x;
        while (x._left != null) {
            x = x._left;
        }
        return x;
    }

    private T remove(String key) {
        BTreeNode b = this;
        while (b != null) {
            if (b._root.key().equals(key)) {
                T val = _root.value();
                _root.setValue(null);
                return val;
            }
            if (b._root.key().compareTo(key) > 0) b = b._right;
            else b = b._left;
        }
        return null;
    }

    private boolean add(Pair pair) {
        if (pair == null) return false;
        BTreeNode b = this;
        boolean exit = false;
        while (!exit) {
            if (b._root.key().equals(pair.key())) return b._root.setValue(pair.value());
            if (b._root.key().compareTo(pair.key()) > 0) {
                if (b._right == null) {
                    b._right = new BTreeNode(pair, null, null, RED);
                    exit = true;
                } else {
                    b = b._right;
                }
            } else {
                if (b._left == null) {
                    b._left = new BTreeNode(pair, null, null, RED);
                    exit = true;
                } else {
                    b = b._left;
                }
            }
        }

        if (isRed(b._right) && !isRed(b._left)) rotateLeft(b);
        if (isRed(b._left) && isRed(b._left._left)) rotateRight(b);
        if (isRed(b._left) && isRed(b._right)) flipColors(b);
        return true;
    }

    // flip the colors of a node and its two children
    private void flipColors(BTreeNode h) {
        h._color = !h._color;
        h._left._color = !h._left._color;
        h._right._color = !h._right._color;
    }

    // make a left-leaning link lean to the right
    private void rotateRight(BTreeNode h) {
        BTreeNode x = h._left;
        h._left = x._right;
        x._right = h;
        x._color = x._right._color;
        x._right._color = RED;

        h._root = x._root;
        h._left = x._left;
        h._right = x._right;
        h._color = x._color;
    }

    // make a right-leaning link lean to the left
    private void rotateLeft(BTreeNode h) {
        BTreeNode x = h._right;
        h._right = x._left;
        x._left = h;
        x._color = x._left._color;
        x._left._color = RED;

        h._root = x._root;
        h._left = x._left;
        h._right = x._right;
        h._color = x._color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private BTreeNode moveRedLeft(BTreeNode b) {
        flipColors(b);
        if (isRed(b._right._left)) {
            rotateRight(b._right);
            rotateLeft(b);
            flipColors(b);
        }
        return b;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private BTreeNode moveRedRight(BTreeNode b) {
        flipColors(b);
        if (isRed(b._left._left)) {
            rotateRight(b);
            flipColors(b);
        }
        return b;
    }

    // restore red-black tree invariant
    private BTreeNode balance(BTreeNode b) {
        if (isRed(b._right)) rotateLeft(b);
        if (isRed(b._left) && isRed(b._left._left)) rotateRight(b);
        if (isRed(b._left) && isRed(b._right)) flipColors(b);
        return b;
    }

    // delete the key-value pair with the minimum key rooted at h
    private BTreeNode deleteMin(BTreeNode b) {
        if (b._left == null)
            return null;

        if (!isRed(b._left) && !isRed(b._left._left))
            b = moveRedLeft(b);

        b._left = deleteMin(b._left);
        return balance(b);
    }
}
