/**
 * A min heap implementation for managing vertices based on their distances.
 * The heap ensures that the vertex with the smallest distance is at the root.
 */
public class MyMinHeap {
    // Array to represent the heap
    private Vertex[] heap;

    // Current number of elements in the heap
    private int size;

    // Maximum capacity of the heap
    private int capacity;

    /**
     * Constructs a min heap with a specified initial capacity.
     *
     * @param capacity the initial capacity of the heap
     */
    public MyMinHeap(int capacity) {
        this.capacity = capacity + 1; // Reserve space for 1-based indexing
        this.size = 0;
        this.heap = new Vertex[this.capacity];
    }

    /**
     * Gets the index of the parent of a given node.
     *
     * @param index the index of the child node
     * @return the index of the parent node
     */
    private int parent(int index) {
        return index / 2;
    }

    /**
     * Gets the index of the left child of a given node.
     *
     * @param index the index of the parent node
     * @return the index of the left child node
     */
    private int leftChild(int index) {
        return 2 * index;
    }

    /**
     * Gets the index of the right child of a given node.
     *
     * @param index the index of the parent node
     * @return the index of the right child node
     */
    private int rightChild(int index) {
        return 2 * index + 1;
    }

    /**
     * Swaps two elements in the heap.
     *
     * @param index1 the index of the first element
     * @param index2 the index of the second element
     */
    private void swap(int index1, int index2) {
        Vertex temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    /**
     * Compares two vertices based on their distances.
     *
     * @param v1 the first vertex to compare
     * @param v2 the second vertex to compare
     * @return a negative value if v1 < v2, positive if v1 > v2, or 0 if they are equal
     */
    private int compare(Vertex v1, Vertex v2) {
        return Double.compare(v1.distance, v2.distance); // Smaller distance is prioritized
    }

    /**
     * Moves an element up the heap to restore the min-heap property.
     *
     * @param index the index of the element to percolate up
     */
    private void percolateUp(int index) {
        while (index > 1 && compare(heap[index], heap[parent(index)]) < 0) {
            swap(index, parent(index)); // Swap with parent
            index = parent(index); // Move up to the parent's index
        }
    }

    /**
     * Moves an element down the heap to restore the min-heap property.
     *
     * @param index the index of the element to percolate down
     */
    private void percolateDown(int index) {
        int smallest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        // Check if left child is smaller than the current node
        if (left <= size && compare(heap[left], heap[smallest]) < 0) {
            smallest = left;
        }

        // Check if right child is smaller than the smallest so far
        if (right <= size && compare(heap[right], heap[smallest]) < 0) {
            smallest = right;
        }

        // If smallest is not the current node, swap and continue
        if (smallest != index) {
            swap(index, smallest);
            percolateDown(smallest);
        }
    }

    /**
     * Doubles the capacity of the heap when it is full.
     */
    private void resizeHeap() {
        capacity *= 2;
        Vertex[] newHeap = new Vertex[capacity];
        System.arraycopy(heap, 0, newHeap, 0, size + 1); // Include placeholder at index 0
        heap = newHeap;
    }

    /**
     * Inserts a new vertex into the heap.
     *
     * @param vertex the Vertex object to insert
     */
    public void insert(Vertex vertex) {
        // Resize the heap if necessary
        if (size == capacity - 1) {
            resizeHeap();
        }

        // Add the vertex at the next available position
        size = size + 1;
        heap[size] = vertex;

        // Restore the heap property
        percolateUp(size);
    }

    /**
     * Retrieves the minimum element (root) without removing it.
     *
     * @return the Vertex object at the root of the heap, or null if the heap is empty
     */
    public Vertex peek() {
        if (size == 0) {
            return null;
        }
        return heap[1]; // Root is at index 1
    }

    /**
     * Removes and returns the minimum element (root) from the heap.
     *
     * @return the Vertex object at the root of the heap, or null if the heap is empty
     */
    public Vertex extractMin() {
        if (size == 0) {
            return null;
        }

        // Store the root element
        Vertex min = heap[1];

        // Replace the root with the last element
        heap[1] = heap[size];
        size = size - 1;

        // Restore the heap property
        percolateDown(1);

        return min;
    }

    /**
     * Checks if the heap is empty.
     *
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Represents a vertex in the graph for Dijkstra's algorithm.
     */
    public static class Vertex {
        int id; // Vertex ID
        double distance; // Distance from the source

        public Vertex(int id, double distance) {
            this.id = id;
            this.distance = distance;
        }
    }
}
