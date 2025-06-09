import java.util.ArrayList;

/**
 * Represents a graph with a grid-based structure and adjacency lists.
 * This graph supports operations for managing nodes, edges, and their properties.
 */
public class Graph {
    private Node[][] grid; // Grid representation of the nodes
    private ArrayList<ArrayList<Dijkstra.Edge>> adjacencyList; // Active adjacency list for graph traversal
    private ArrayList<ArrayList<Dijkstra.Edge>> originalAdjacencyList; // Backup adjacency list for restoring edges
    private int width; // Width of the grid
    private int height; // Height of the grid
    private int counter; // Counter to track unrevealed nodes
    private boolean hasUnknownNodes; // Flag to indicate if there are unknown nodes
    private boolean isAllRevealed; // Flag to indicate if all nodes are revealed

    /**
     * Constructs a {@code Graph} with the specified width and height.
     * Initializes the grid, adjacency lists, and other graph properties.
     *
     * @param width  The width of the grid.
     * @param height The height of the grid.
     */
    public Graph(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Node[width][height];
        this.isAllRevealed = false;
        this.hasUnknownNodes = false;
        this.counter = width * height;

        // Initialize adjacency lists for each node
        int nodeCount = width * height;
        adjacencyList = new ArrayList<>();
        originalAdjacencyList = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            adjacencyList.add(new ArrayList<>());
            originalAdjacencyList.add(new ArrayList<>());
        }
    }

    /**
     * Returns the width of the grid.
     *
     * @return The width of the grid.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the grid.
     *
     * @return The height of the grid.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks if all nodes in the grid are revealed.
     *
     * @return {@code true} if all nodes are revealed; {@code false} otherwise.
     */
    public boolean isAllRevealed() {
        return isAllRevealed;
    }

    /**
     * Checks if all nodes in the grid have been revealed.
     */
    public void checkAllNodesRevealed() {
        if (counter == 0) {
            this.isAllRevealed = true;
        }
    }

    /**
     * Checks if there exists a node with color bigger than 1 in the initialization.
     *
     * @return {@code true} if such node has been initialized; {@code false} otherwise.
     */
    public boolean hasUnknownNodes() {
        return hasUnknownNodes;
    }

    /**
     * Marks the existence of a node with color bigger than 1 in the initialization.
     */
    public void detectedUnknownNodes() {
        this.hasUnknownNodes = true;
    }

    /**
     * Adds a node to the grid at the specified coordinates with the given color.
     *
     * @param x     The X-coordinate of the node.
     * @param y     The Y-coordinate of the node.
     * @param color The color/type of the node.
     */
    public void addNode(int x, int y, int color) {
        grid[x][y] = new Node(x, y, color);
    }

    /**
     * Maps a {@code Node} to its corresponding index in the adjacency list.
     *
     * @param node The {@code Node} to map.
     * @return The 1D index of the node in the adjacency list.
     */
    public int mapToIndex(Node node) {
        return node.getY() + node.getX() * height;
    }

    /**
     * Maps a 1D index to its corresponding 2D coordinates in the grid.
     *
     * @param index The 1D index to map.
     * @return A 2-element array containing the X and Y coordinates.
     */
    public int[] mapToCoordinates(int index) {
        int width = grid[0].length;
        return new int[]{index / width, index % width};
    }

    /**
     * Adds a bidirectional edge between two nodes with a specified travel time.
     *
     * @param node1      The first node.
     * @param node2      The second node.
     * @param travelTime The weight of the edge (travel time).
     */
    public void addEdge(Node node1, Node node2, double travelTime) {
        int index1 = mapToIndex(node1);
        int index2 = mapToIndex(node2);

        // Add edges to both the active and original adjacency lists
        adjacencyList.get(index1).add(new Dijkstra.Edge(index2, travelTime));
        adjacencyList.get(index2).add(new Dijkstra.Edge(index1, travelTime));

        originalAdjacencyList.get(index1).add(new Dijkstra.Edge(index2, travelTime));
        originalAdjacencyList.get(index2).add(new Dijkstra.Edge(index1, travelTime));
    }

    /**
     * Retrieves a node from the grid based on its coordinates.
     *
     * @param x The X-coordinate of the node.
     * @param y The Y-coordinate of the node.
     * @return The {@code Node} object at the specified coordinates.
     */
    public Node getNode(int x, int y) {
        return grid[x][y];
    }

    /**
     * Returns the grid representation of the graph.
     *
     * @return A 2D array of {@code Node} objects.
     */
    public Node[][] getGrid() {
        return grid;
    }

    /**
     * Returns the active adjacency list used for graph traversal.
     *
     * @return An {@code ArrayList} of adjacency lists.
     */
    public ArrayList<ArrayList<Dijkstra.Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Returns the original adjacency list for restoring edges.
     *
     * @return An {@code ArrayList} of original adjacency lists.
     */
    public ArrayList<ArrayList<Dijkstra.Edge>> getOriginalAdjacencyList() {
        return originalAdjacencyList;
    }

    /**
     * Removes an edge between two nodes by effectively setting its weight to infinity.
     *
     * @param node1 The first node.
     * @param node2 The second node.
     */
    public void setEdgeToInfinity(Node node1, Node node2) {
        int index1 = mapToIndex(node1);
        int index2 = mapToIndex(node2);

        // Remove edges between the nodes in the adjacency list
        adjacencyList.get(index1).removeIf(edge -> edge.to == index2);
        adjacencyList.get(index2).removeIf(edge -> edge.to == index1);
    }

    /**
     * Reveals a node, updating its status and clearing edges if the node is impassable.
     *
     * @param node The node to reveal.
     */
    public void revealNode(Node node) {
        if (!node.isRevealed()) {
            node.reveal();
            counter--;

            // Remove all edges if the node is impassable (color >= 2)
            if (node.getColor() >= 2) {
                int nodeIndex = mapToIndex(node);
                adjacencyList.set(nodeIndex, new ArrayList<>()); // Clear edges
            }
        }
    }

    /**
     * Checks if the given coordinates are within the bounds of the grid.
     *
     * @param x The X-coordinate to check.
     * @param y The Y-coordinate to check.
     * @return {@code true} if the coordinates are within bounds; {@code false} otherwise.
     */
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Checks if a point is within a circular radius from a center point.
     *
     * @param centerX The X-coordinate of the circle's center.
     * @param centerY The Y-coordinate of the circle's center.
     * @param x       The X-coordinate of the point.
     * @param y       The Y-coordinate of the point.
     * @param radius  The radius of the circle.
     * @return {@code true} if the point is within the circle; {@code false} otherwise.
     */
    public boolean isWithinCircle(int centerX, int centerY, int x, int y, int radius) {
        return (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) <= radius * radius;
    }

    /**
     * Reveals all nodes within a specified radius from a center node.
     * Only nodes within the circle and grid bounds are revealed.
     *
     * @param currentX The X-coordinate of the center node.
     * @param currentY The Y-coordinate of the center node.
     * @param radius   The radius within which nodes are revealed.
     */
    public void revealNodes(int currentX, int currentY, int radius) {
        for (int i = currentX - radius; i <= currentX + radius; i++) {
            for (int j = currentY - radius; j <= currentY + radius; j++) {
                if (isInBounds(i, j) && isWithinCircle(currentX, currentY, i, j, radius)) {
                    Node node = grid[i][j];
                    revealNode(node);
                }
            }
        }
    }

    /**
     * Unlocks nodes of a specific type and restores their edges from the original adjacency list.
     *
     * @param unlockType The type of nodes to unlock.
     */
    public void unlockNodes(int unlockType) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Node node = grid[i][j];
                if (node.getOriginalColor() == unlockType) {
                    node.setColor(0); // Change node type to passable

                    int nodeIndex = mapToIndex(node);

                    // Restore edges for the unlocked node
                    adjacencyList.set(nodeIndex, new ArrayList<>(originalAdjacencyList.get(nodeIndex)));
                }
            }
        }
    }

    /**
     * Finds the best unlock option from a list of options using Dijkstra's algorithm.
     * Determines the option that results in the shortest travel time.
     *
     * @param options         A list of unlock options.
     * @param source          The source node index for the pathfinding operation.
     * @param destination     The destination node index.
     * @param unlockedOptions A hash map of already unlocked options to avoid duplication.
     * @return The best unlock option based on the shortest path, or {@code -1} if no option is valid.
     */
    public int findBestUnlockOption(ArrayList<Integer> options, int source, int destination, MyHashMap unlockedOptions) {
        int bestOption = -1;
        double shortestTime = Double.MAX_VALUE;

        for (int option : options) {
            if (unlockedOptions.contains(Integer.toString(option))) {
                continue;
            }

            // Clone the graph to avoid permanent changes
            Graph tempGraph = this.clone();
            tempGraph.unlockNodes(option);

            // Run Dijkstra's algorithm on the modified graph
            DijkstraResult result = Dijkstra.dijkstra(tempGraph.getAdjacencyList(), source, destination);
            double distance = result.getDistances()[destination];

            if (distance < shortestTime) {
                shortestTime = distance;
                bestOption = option;
            }
        }

        return bestOption;
    }

    /**
     * Creates a deep copy of the current graph, including nodes and adjacency lists.
     *
     * @return A new {@code Graph} object that is a clone of the current graph.
     */
    @Override
    public Graph clone() {
        Graph clonedGraph = new Graph(this.grid.length, this.grid[0].length);

        // Deep copy nodes
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                Node originalNode = grid[x][y];
                clonedGraph.addNode(x, y, originalNode.getColor());
            }
        }

        // Deep copy adjacency lists
        for (int i = 0; i < adjacencyList.size(); i++) {
            clonedGraph.adjacencyList.set(i, new ArrayList<>(adjacencyList.get(i)));
            clonedGraph.originalAdjacencyList.set(i, new ArrayList<>(originalAdjacencyList.get(i)));
        }

        return clonedGraph;
    }

    /**
     * Returns a string representation of the graph's grid.
     * Displays the grid as rows and columns with node types.
     *
     * @return A string representation of the grid.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Grid Representation:\n");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Node node = grid[x][y];
                sb.append(node == null ? "X " : node.getColor() + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the adjacency list for the graph.
     *
     * @return A string representation of the adjacency list.
     */
    public String adjacencyListToString() {
        StringBuilder sb = new StringBuilder("Adjacency List:\n");
        for (int i = 0; i < adjacencyList.size(); i++) {
            sb.append("Node ").append(i).append(": ");
            for (Dijkstra.Edge edge : adjacencyList.get(i)) {
                sb.append(edge.to).append("(").append(edge.weight).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
