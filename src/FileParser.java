import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code FileParser} class is responsible for reading and parsing input files
 * to construct a {@code Graph} and a list of {@code Mission} objects.
 * It processes three input files:
 * <ul>
 *     <li>Land file: Describes the grid and its nodes.</li>
 *     <li>Travel file: Defines the edges and travel times between nodes.</li>
 *     <li>Mission file: Specifies the missions and objectives.</li>
 * </ul>
 */
public class FileParser {
    private String landFile; // Path to the land file
    private String travelFile; // Path to the travel file
    private String missionFile; // Path to the mission file

    /**
     * Constructs a {@code FileParser} object with the specified file paths.
     *
     * @param landFile    Path to the land file.
     * @param travelFile  Path to the travel file.
     * @param missionFile Path to the mission file.
     */
    public FileParser(String landFile, String travelFile, String missionFile) {
        this.landFile = landFile;
        this.travelFile = travelFile;
        this.missionFile = missionFile;
    }

    /**
     * Parses the land file to create a {@code Graph} object.
     * The land file defines the grid dimensions and the nodes with their respective properties.
     *
     * @param landFile Path to the land file.
     * @return A {@code Graph} object representing the grid and its nodes.
     */
    private static Graph parseLandFile(String landFile) {
        Graph graph = null;
        try (BufferedReader br = new BufferedReader(new FileReader(landFile))) {
            // Read the first line to get grid dimensions
            String[] dimensions = br.readLine().split(" ");
            int width = Integer.parseInt(dimensions[0]); // Grid width
            int height = Integer.parseInt(dimensions[1]); // Grid height
            graph = new Graph(width, height);

            // Read each line to define the nodes
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");
                int x = Integer.parseInt(data[0]); // X-coordinate of the node
                int y = Integer.parseInt(data[1]); // Y-coordinate of the node
                int color = Integer.parseInt(data[2]); // Node property
                graph.addNode(x, y, color); // Add the node to the graph
                if (color >= 2) {
                    graph.detectedUnknownNodes();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    /**
     * Parses the travel file to define edges and travel times between nodes in the graph.
     * The file specifies connectivity and travel times for pairs of nodes.
     *
     * @param travelFile Path to the travel file.
     * @param graph      The {@code Graph} object to which the edges will be added.
     */
    private static void parseTravelFile(String travelFile, Graph graph) {
        try (BufferedReader br = new BufferedReader(new FileReader(travelFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");
                String[] coordinates = data[0].split(",");
                String[] coordinates1 = coordinates[0].split("-");
                String[] coordinates2 = coordinates[1].split("-");
                double travelTime = Double.parseDouble(data[1]); // Travel time between nodes

                int x1 = Integer.parseInt(coordinates1[0]);
                int y1 = Integer.parseInt(coordinates1[1]);
                int x2 = Integer.parseInt(coordinates2[0]);
                int y2 = Integer.parseInt(coordinates2[1]);

                // Retrieve nodes from the graph
                Node node1 = graph.getNode(x1, y1);
                Node node2 = graph.getNode(x2, y2);

                // Handle impassable nodes
                if (node1.getColor() == 1 || node2.getColor() == 1) {
                    graph.setEdgeToInfinity(node1, node2); // Mark edge as impassable
                } else {
                    graph.addEdge(node1, node2, travelTime); // Add edge with travel time
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the mission file to create a list of {@code Mission} objects.
     * The file defines the visibility radius, starting position, and objectives.
     *
     * @param filePath Path to the mission file.
     * @return A list of {@code Mission} objects.
     */
    private static ArrayList<Mission> parseMissionFile(String filePath) {
        ArrayList<Mission> missions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read visibility radius
            int radius = Integer.parseInt(br.readLine().trim());

            // Read starting node coordinates
            String[] startCoords = br.readLine().trim().split(" ");
            int startX = Integer.parseInt(startCoords[0]);
            int startY = Integer.parseInt(startCoords[1]);

            // Read mission objectives
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                int x = Integer.parseInt(parts[0]); // Objective X-coordinate
                int y = Integer.parseInt(parts[1]); // Objective Y-coordinate

                if (parts.length > 2) { // If wizard help is provided
                    ArrayList<Integer> options = new ArrayList<>();
                    for (int i = 2; i < parts.length; i++) {
                        options.add(Integer.parseInt(parts[i]));
                    }
                    missions.add(new Mission(x, y, radius, options));
                } else { // Type 1 mission (no wizard help)
                    missions.add(new Mission(x, y, radius, null));
                }
            }

            // Add the starting position as the first mission
            missions.add(0, new Mission(startX, startY, radius, null));

        } catch (IOException e) {
            System.err.println("Error reading mission file: " + e.getMessage());
            e.printStackTrace();
        }
        return missions;
    }

    /**
     * Parses all input files and constructs a {@code FileParserResult}.
     * This includes a graph with nodes and edges and a list of missions.
     *
     * @return A {@code FileParserResult} containing the parsed {@code Graph} and {@code Mission} list.
     */
    public FileParserResult parseFiles() {
        Graph graph = parseLandFile(landFile); // Parse the land file to create the graph
        parseTravelFile(travelFile, graph); // Add edges from the travel file
        ArrayList<Mission> missions = parseMissionFile(missionFile); // Parse mission file
        return new FileParserResult(graph, missions); // Return the combined result
    }
}
