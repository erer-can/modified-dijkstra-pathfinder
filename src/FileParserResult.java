import java.util.ArrayList;

/**
 * Represents the result of parsing input files using the {@code FileParser}.
 * This class encapsulates:
 * <ul>
 *     <li>A {@code Graph} object representing the parsed nodes and edges of the grid.</li>
 *     <li>A list of {@code Mission} objects specifying mission details and objectives.</li>
 * </ul>
 */
public class FileParserResult {
    private Graph graph; // The parsed graph containing nodes and edges
    private ArrayList<Mission> missions; // The list of parsed missions

    /**
     * Constructs a {@code FileParserResult} object.
     *
     * @param graph    The {@code Graph} object representing the parsed grid and its connectivity.
     * @param missions A list of {@code Mission} objects specifying the parsed missions.
     */
    public FileParserResult(Graph graph, ArrayList<Mission> missions) {
        this.graph = graph;
        this.missions = missions;
    }

    /**
     * Retrieves the parsed graph from the result.
     *
     * @return The {@code Graph} object containing nodes and edges parsed from the input files.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Retrieves the list of parsed missions from the result.
     *
     * @return An {@code ArrayList<Mission>} containing the parsed mission details.
     */
    public ArrayList<Mission> getMissions() {
        return missions;
    }
}
