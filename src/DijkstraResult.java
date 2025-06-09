import java.util.ArrayList;

/**
 * Represents the result of Dijkstra's shortest path algorithm.
 * This class encapsulates:
 * <ul>
 *     <li>The shortest distances from the source to all vertices.</li>
 *     <li>The reconstructed shortest path from the source to the destination.</li>
 * </ul>
 */
public class DijkstraResult {
    private double[] distances; // Array storing the shortest distances from the source to each vertex
    private ArrayList<Integer> path; // List representing the shortest path from source to destination

    /**
     * Constructs a {@code DijkstraResult} object.
     *
     * @param distances An array of shortest distances from the source vertex to all other vertices.
     *                  Each index corresponds to a vertex, and the value represents the shortest distance.
     * @param path      An {@code ArrayList} of integers representing the sequence of vertices
     *                  in the shortest path from the source to the destination.
     */
    public DijkstraResult(double[] distances, ArrayList<Integer> path) {
        this.distances = distances;
        this.path = path;
    }

    /**
     * Returns the array of shortest distances from the source to each vertex.
     *
     * @return A {@code double[]} where each index corresponds to a vertex, and the value
     *         represents the shortest distance from the source.
     */
    public double[] getDistances() {
        return distances;
    }

    /**
     * Returns the reconstructed shortest path from the source to the destination.
     *
     * @return An {@code ArrayList<Integer>} containing the sequence of vertex indices
     *         representing the shortest path. If no path exists, the list will be empty.
     */
    public ArrayList<Integer> getPath() {
        return path;
    }
}
