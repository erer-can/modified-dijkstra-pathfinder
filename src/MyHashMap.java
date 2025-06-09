/**
 * A custom implementation of a hash map that supports insertion, deletion, and lookup operations.
 * This hash map uses chaining for collision handling and dynamically resizes when the load factor exceeds a threshold.
 */
public class MyHashMap {

    /**
     * A node in the hash map's linked list chain.
     * Represents a key-value pair along with a reference to the next node in the chain.
     */
    private class MyHashNode {
        String key; // Key of the key-value pair
        Object value; // Value of the key-value pair
        MyHashNode next; // Reference to the next node in the chain

        /**
         * Constructs a hash node with a key and value.
         *
         * @param key   the key of the node
         * @param value the value of the node
         */
        public MyHashNode(String key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    // Array of chains for storing key-value pairs
    private MyHashNode[] chains;

    // Current capacity of the hash map
    private int capacity;

    // Number of key-value pairs stored in the hash map
    private int size;

    // Load factor threshold for resizing
    private final double LOAD_FACTOR = 0.75;

    /**
     * Constructs a hash map with a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the hash map
     */
    public MyHashMap(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;
        this.chains = new MyHashNode[capacity];
    }

    /**
     * Hash function to compute the index for a given key.
     *
     * @param key the key to hash
     * @return the index corresponding to the key
     */
    private int hashFunction(String key) {
        int hash = 0;

        // Compute hash using a simple polynomial rolling hash
        for (char c : key.toCharArray()) {
            hash = (hash * 127 + c) % capacity;
        }

        // Ensure the hash is non-negative
        if (hash < 0) {
            hash += capacity;
        }
        return hash;
    }

    /**
     * Inserts a key-value pair into the hash map.
     * If the key already exists, the insertion is ignored.
     *
     * @param key   the key to insert
     * @param value the value associated with the key
     */
    public void insert(String key, Object value) {
        int index = hashFunction(key);
        MyHashNode head = chains[index];

        // Check if the key already exists
        if (contains(key)) {
            return;
        }

        // Create a new node for the key-value pair
        MyHashNode newNode = new MyHashNode(key, value);

        // Insert the new node at the head of the chain
        if (head == null) {
            chains[index] = newNode;
        } else {
            // Traverse to the end of the chain
            while (head.next != null) {
                head = head.next;
            }
            head.next = newNode;
        }

        size = size + 1;

        // Resize the hash map if the load factor is exceeded
        if (((double) size / capacity) > LOAD_FACTOR) {
            rehash();
        }
    }

    /**
     * Retrieves the value associated with a given key.
     *
     * @param key the key to search for
     * @return the value associated with the key, or null if the key does not exist
     */
    public Object find(String key) {
        int index = hashFunction(key);
        MyHashNode head = chains[index];

        // Traverse the chain to find the key
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    /**
     * Checks whether a given key exists in the hash map.
     *
     * @param key the key to search for
     * @return true if the key exists, false otherwise
     */
    public boolean contains(String key) {
        return find(key) != null;
    }

    /**
     * Removes a key-value pair from the hash map.
     *
     * @param key the key to remove
     */
    public void remove(String key) {
        int index = hashFunction(key);
        MyHashNode head = chains[index];

        // If the chain is empty, nothing to remove
        if (head == null) {
            return;
        }

        // If the key is at the head of the chain
        if (head.key.equals(key)) {
            chains[index] = head.next;
            size = size - 1;
            return;
        }

        // Traverse the chain to find and remove the key
        MyHashNode prev = head;
        head = head.next;

        while (head != null) {
            if (head.key.equals(key)) {
                prev.next = head.next;
                size = size - 1;
                return;
            }
            prev = head;
            head = head.next;
        }
    }

    /**
     * Resizes the hash map by doubling its capacity and rehashing all existing key-value pairs.
     */
    private void rehash() {
        MyHashNode[] oldChain = chains;

        capacity = 2 * capacity;
        chains = new MyHashNode[capacity];
        size = 0;

        // Rehash all existing key-value pairs
        for (MyHashNode head : oldChain) {
            while (head != null) {
                insert(head.key, head.value);
                head = head.next;
            }
        }
    }

    /**
     * Returns an array of all keys in the hash map.
     *
     * @return an array of keys
     */
    public String[] keys() {
        String[] keysArray = new String[size];
        int index = 0;

        // Traverse all chains to collect keys
        for (MyHashNode chain : chains) {
            MyHashNode current = chain;
            while (current != null) {
                keysArray[index++] = current.key;
                current = current.next;
            }
        }
        return keysArray;
    }

    /**
     * Returns an array of all values in the hash map.
     *
     * @return an array of values
     */
    public Object[] values() {
        Object[] valuesArray = new Object[size];
        int index = 0;

        // Traverse all chains to collect values
        for (MyHashNode chain : chains) {
            MyHashNode current = chain;
            while (current != null) {
                valuesArray[index++] = current.value;
                current = current.next;
            }
        }
        return valuesArray;
    }

    /**
     * Returns a string representation of the hash map, showing all chains and their contents.
     *
     * @return a formatted string representation of the hash map
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Traverse each chain and append its contents
        for (int i = 0; i < capacity; i++) {
            sb.append("Chain ").append(i).append(": ");
            MyHashNode head = chains[i];
            while (head != null) {
                sb.append("(").append(head.key).append(", ").append(head.value).append(") -> ");
                head = head.next;
            }
            sb.append("null\n");
        }

        return sb.toString();
    }
}
