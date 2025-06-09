import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code Execution} class manages the entire mission execution process
 * based on a parsed graph and missions. It dynamically reveals nodes,
 * calculates paths, handles obstacles, and logs progress to an output file.
 */
public class Execution {
    private Graph graph; // Graph representing the mission environment
    private ArrayList<Mission> missions; // List of missions to execute
    private OutputWriter outputWriter; // Logs execution events
    private MyHashMap unlockedOptions; // Tracks unlocked options during execution

    /**
     * Constructs an {@code Execution} object by parsing the input files for the graph and missions.
     *
     * @param landFile    Path to the land file.
     * @param travelFile  Path to the travel file.
     * @param missionFile Path to the mission file.
     */
    public Execution(String landFile, String travelFile, String missionFile, String outputFile) throws IOException {
        FileParser fileParser = new FileParser(landFile, travelFile, missionFile);
        FileParserResult items = fileParser.parseFiles();
        this.graph = items.getGraph(); // Initialize the graph from parsed data
        this.missions = items.getMissions(); // Initialize the missions list
        this.outputWriter = new OutputWriter(); // Set up the output writer for logging
        outputWriter.createFile(outputFile); // Create the output file
        this.unlockedOptions = new MyHashMap(20); // Initialize the hash map for unlocked options
    }

    /**
     * Executes the mission sequence and logs the progress to the specified output file.
     */
    public void execute() throws IOException {
        // Remove the first mission and set it as the starting point
        Mission startingMission = missions.remove(0);
        int currentX = startingMission.getX();
        int currentY = startingMission.getY();
        Node currentNode = graph.getNode(currentX, currentY);

        // Process each mission in turn
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            int destX = mission.getX();
            int destY = mission.getY();
            Node destinationNode = graph.getNode(destX, destY);

            // Initialize prevX, prevY at the start of the mission
            int prevX = currentX;
            int prevY = currentY;

            while (true) {
                // Reveal nodes around the current position if not all revealed
                if (!graph.isAllRevealed() && graph.hasUnknownNodes()) {
                    graph.revealNodes(currentX, currentY, mission.getRadius());
                }

                // Compute path using Dijkstra's algorithm
                int startIndex = graph.mapToIndex(currentNode);
                int destIndex = graph.mapToIndex(destinationNode);
                DijkstraResult result = Dijkstra.dijkstra(graph.getAdjacencyList(), startIndex, destIndex);
                ArrayList<Integer> path = result.getPath();

                // If no path is found, log and break the loop
                if (path.isEmpty()) {
                    outputWriter.logPathImpassable();
                    break;
                }

                boolean recalculationNeeded = false;
                boolean firstStep = true;

                // Follow the computed path
                for (int index : path) {
                    graph.checkAllNodesRevealed();
                    int[] coords = graph.mapToCoordinates(index);

                    // If we are already on this node, skip moving
                    if (coords[0] == currentX && coords[1] == currentY) {
                        continue;
                    }

                    // Move to the next node on the path
                    currentX = coords[0];
                    currentY = coords[1];
                    currentNode = graph.getNode(currentX, currentY);
                    outputWriter.logMovement(currentX, currentY);

                    // Attempt reveals if the map is not fully revealed and there are unknown nodes
                    if (!graph.isAllRevealed() && graph.hasUnknownNodes()) {
                        if (firstStep) {
                            firstStep = false;
                            // Reveal nodes within the mission's radius
                            recalculationNeeded = checkRadiusAndReveal(graph, currentX, currentY, mission.getRadius(), path);
                        } else {
                            // Dynamically reveal nodes based on movement direction
                            int dx = currentX - prevX;
                            int dy = currentY - prevY;
                            recalculationNeeded = checkDynamicReveal(graph, currentX, currentY, prevX, prevY, dx, dy, mission.getRadius(), path);
                        }
                    }

                    // If recalculation is needed due to obstacles, log and break
                    if (recalculationNeeded) {
                        outputWriter.logPathImpassable();
                        break;
                    }

                    // Check if the destination is reached
                    if (currentX == destX && currentY == destY) {
                        outputWriter.logObjectiveReached(i + 1);
                        break;
                    }

                    // Update prevX, prevY for the next step
                    prevX = currentX;
                    prevY = currentY;
                }

                // If destination is reached, move to the next mission
                if (currentX == destX && currentY == destY) {
                    break;
                }
            }

            // If wizard assistance is available and not the last mission
            if (mission.hasWizardOptions() && i < missions.size() - 1) {
                Mission nextMission = missions.get(i + 1);
                int nextX = nextMission.getX();
                int nextY = nextMission.getY();
                Node nextNode = graph.getNode(nextX, nextY);

                int bestOption = graph.findBestUnlockOption(
                        mission.getWizardOptions(),
                        graph.mapToIndex(currentNode),
                        graph.mapToIndex(nextNode),
                        unlockedOptions
                );

                outputWriter.logWizardChoice(bestOption);
                graph.unlockNodes(bestOption);
                unlockedOptions.insert(Integer.toString(bestOption), bestOption);
            }
        }

        // Write all logs to the output file
        outputWriter.writeToFile();
    }

    /**
     * Reveals nodes within a circular radius and checks for recalculation requirements.
     *
     * @param graph  The graph being processed.
     * @param currentX Current X-coordinate.
     * @param currentY Current Y-coordinate.
     * @param radius Radius of reveal.
     * @param path Path for validation.
     * @return {@code true} if recalculation is needed; {@code false} otherwise.
     */
    private boolean checkRadiusAndReveal(Graph graph, int currentX, int currentY, int radius, ArrayList<Integer> path) {
        boolean recalculationNeeded = false;
        for (int k = currentX - radius; k <= currentX + radius; k++) {
            for (int j = currentY - radius; j <= currentY + radius; j++) {
                if (graph.isInBounds(k, j) && graph.isWithinCircle(currentX, currentY, k, j, radius)) {
                    recalculationNeeded |= revealAndCheck(graph, k, j, path);
                }
            }
        }
        return recalculationNeeded;
    }

    /**
     * Dynamically reveals nodes based on the movement direction and checks for obstacles.
     * This method ensures that nodes within the circular visibility radius, which
     * become newly visible due to movement, are revealed and checked for impassability.
     *
     * @param graph   The graph representing the environment.
     * @param currentX The current X-coordinate of the node.
     * @param currentY The current Y-coordinate of the node.
     * @param prevX    The previous X-coordinate of the node.
     * @param prevY    The previous Y-coordinate of the node.
     * @param dx       The change in X-coordinate during the movement (-1, 0, or 1).
     * @param dy       The change in Y-coordinate during the movement (-1, 0, or 1).
     * @param radius   The visibility radius for revealing nodes.
     * @param path     The current path being followed.
     * @return {@code true} if recalculation of the path is needed due to impassable nodes; {@code false} otherwise.
     */
    private boolean checkDynamicReveal(Graph graph, int currentX, int currentY, int prevX, int prevY, int dx, int dy, int radius, ArrayList<Integer> path) {
        boolean recalculationNeeded = false;

        // Check newly visible nodes depending on movement direction
        if (dx == 1) { // Moved right
            // Traverse nodes from the previous X to the current X + radius
            for (int k = prevX; k <= currentX + radius; k++) {
                for (int j = currentY - radius; j <= currentY + radius; j++) {
                    // Reveal and check if the node is within bounds and the circle
                    if (graph.isInBounds(k, j) && graph.isWithinCircle(currentX, currentY, k, j, radius)) {
                        recalculationNeeded |= revealAndCheck(graph, k, j, path);
                    }
                }
            }
        } else if (dx == -1) { // Moved left
            // Traverse nodes from the current X - radius to the previous X
            for (int k = currentX - radius; k <= prevX; k++) {
                for (int j = currentY - radius; j <= currentY + radius; j++) {
                    if (graph.isInBounds(k, j) && graph.isWithinCircle(currentX, currentY, k, j, radius)) {
                        recalculationNeeded |= revealAndCheck(graph, k, j, path);
                    }
                }
            }
        } else if (dy == 1) { // Moved up
            // Traverse nodes from the previous Y to the current Y + radius
            for (int k = currentX - radius; k <= currentX + radius; k++) {
                for (int j = prevY; j <= currentY + radius; j++) {
                    if (graph.isInBounds(k, j) && graph.isWithinCircle(currentX, currentY, k, j, radius)) {
                        recalculationNeeded |= revealAndCheck(graph, k, j, path);
                    }
                }
            }
        } else if (dy == -1) { // Moved down
            // Traverse nodes from the current Y - radius to the previous Y
            for (int k = currentX - radius; k <= currentX + radius; k++) {
                for (int j = currentY - radius; j <= prevY; j++) {
                    if (graph.isInBounds(k, j) && graph.isWithinCircle(currentX, currentY, k, j, radius)) {
                        recalculationNeeded |= revealAndCheck(graph, k, j, path);
                    }
                }
            }
        }

        return recalculationNeeded;
    }

    /**
     * Reveals a node at the specified coordinates and checks if it requires a path recalculation.
     * This method ensures that newly revealed nodes are checked for impassability and that the path is adjusted accordingly.
     *
     * @param graph The graph representing the environment.
     * @param k     The X-coordinate of the node.
     * @param j     The Y-coordinate of the node.
     * @param path  The current path being followed.
     * @return {@code true} if the node is impassable and affects the current path; {@code false} otherwise.
     */
    private boolean revealAndCheck(Graph graph, int k, int j, ArrayList<Integer> path) {
        // Get the node at the specified coordinates
        Node node = graph.getGrid()[k][j];

        // Check if the node is not revealed
        if (!node.isRevealed()) {
            // Reveal the node
            graph.revealNode(node);

            // Map the node to its index in the adjacency list
            int nodeIndex = graph.mapToIndex(node);

            // Check if the node is part of the path and is impassable
            if (path.contains(nodeIndex) && node.getOriginalColor() >= 1) {
                return true; // Path recalculation is needed
            }
        }

        return false;
    }
}
