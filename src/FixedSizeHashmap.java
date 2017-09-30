/**
 * FixedSizeHashMap - A fixed-size hash map that associates string keys
 * with arbitrary data objects using only primitive types.
 *
 * @author Amay Saxena
 */

/**
 * A fixed-size hash map that associates string keys with arbitrary
 * data objects using only primitive types.
 * @param <T> - the Object type to be stored in the hashmap
 */
public class FixedSizeHashmap<T> {
    private int _capacity;
    private int _numItems;
    private Object[] _items;
    private int _arraySize;

    public FixedSizeHashmap(int size) {
        _capacity = size;
        _numItems = 0;

        // Set _arraySize to the smallest power of two greater than
        // or equal to _capacity
        _arraySize = 1;
        while (_arraySize < _capacity) {
            _arraySize <<= 1;
        }

        _items = new Object[_arraySize];
    }

    /**
     * CREDIT: http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/HashMap.java#HashMap.indexFor%28int,int%29
     * With the backing array size being a power of two, the
     * modulo operation to obtain an array index can be done very cheaply using
     * a bitwise and.
     * @param hash the hashcode being used
     * @return a number in the range [0, _arraySize)
     */
    private int index(int hash) {
        return hash & (_arraySize - 1);
    }

    /**
     * A supplementary hashcode that protects against poor hascodes. Since the backing
     * array size is a power of two, poor hashcodes can lead to a lot of collisions
     * when they do not differ in lower bits.
     * @param h the hascode to be improved.
     * @return a new hashmap that minizes collisions in lower bits.
     */
    private int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Add a (key, value) pair to the hasmap.
     * @param key the String key to be added.
     * @param value the value to be associated with key.
     * @return boolean indicting success of the operation.
     */
    public boolean set(String key, T value) {
        if (_numItems >= _capacity) {
            return false;
        }

        Pair p = new Pair(key, value);
        int index = index(hash(key.hashCode()));
        LinkedList listAtIndex = (LinkedList) _items[index];

        if (listAtIndex == null) {
            _items[index] = new LinkedList(p);
        } else {
            listAtIndex.add(p);
        }
        _numItems++;
        return true;
    }

    /**
     * Returns the size of the hashmap. i.e. the number of (key, value) pairs stored.
     * @return the number of (key, value) pairs in the hashmap.
     */
    public int size() {
        return _numItems;
    }

    /**
     * Return the value associated with the argument key in this hashmap.
     * @param key the query key.
     * @return value associated with key.
     */
    public T get(String key) {
        int index = index(hash(key.hashCode()));
        LinkedList listAtIndex = (LinkedList) _items[index];
        if (listAtIndex == null) return null;
        return listAtIndex.get(key);
    }

    /**
     * Remove and return the value associated with this key.
     * @param key the query key.
     * @return the value associated with the key.
     */
    public T delete(String key) {
        int index = index(hash(key.hashCode()));
        LinkedList listAtIndex = (LinkedList) _items[index];
        if (listAtIndex == null) return null;

        _numItems--;
        return listAtIndex.remove(key);
    }

    /**
     * Return the load factor of the current hashmap.
     * @return load factor of current hashmap.
     */
    public float load() {
        return ((float) _numItems) / ((float) _capacity);
    }

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

        void setValue(T val) {
            _val = val;
        }
    }

    private class LinkedList {
        LLNode sentinel;
        int _size;

        LinkedList(Pair p) {
            sentinel = new LLNode(null);
            add(p);
        }

        LLNode sentinel() {
            return sentinel;
        }

        /**
         * Add the pair to this linked list.
         * @param pair (key, value) Pair object to be added.
         * @return boolean indicating success of operation.
         */
        boolean add(Pair pair) {
            if (pair == null) {
                return false;
            }
            LLNode p = sentinel()._next;
            while (p != null) {
                if (p._val.key().equals(pair.key())) {
                    p._val.setValue(pair.value());
                    _size++;
                    return true;
                }
                p = p._next;
            }
            sentinel._next = new LLNode(pair, sentinel._next);
            return true;
        }

        /**
         * Return value associated with key in this linked list.
         * @param key the queried key.
         * @return the value associated with this key.
         */
        T get(String key) {
            LLNode p = sentinel()._next;
            while (p != null) {
                if (p._val.key().equals(key)) return p._val.value();
                p = p._next;
            }
            return null;
        }

        /**
         * Remove and return value associated with this key.
         * @param key the queried key.
         * @return value associated with this key.
         */
        T remove(String key) {
            LLNode p = sentinel();
            while (p._next != null) {
                if (p._next._val.key().equals(key)) {
                    Pair toRemove = p._next._val;
                    p._next = p._next._next;
                    _size--;
                    return toRemove.value();
                }
                p = p._next;
            }
            return null;
        }
    }

    /**
     * Helper class for linked list node objects.
     */
    private class LLNode {
        Pair _val;
        LLNode _next;

        LLNode(Pair val, LLNode next) {
            _val = val;
            _next = next;
        }

        LLNode(Pair val) {
            _val = val;
            _next = null;
        }
    }
}
