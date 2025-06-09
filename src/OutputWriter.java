import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code OutputWriter} class is responsible for logging events and writing the logs to an output file.
 * It stores log messages in an {@code ArrayList} and provides methods to log various events such as
 * node movements, reaching objectives, or handling impassable paths.
 */
public class OutputWriter {
    private ArrayList<String> log; // Stores all log messages sequentially
    private File outputFile; // Stores the reference to the output file

    /**
     * Constructs an {@code OutputWriter} object.
     * Initializes the log storage to hold event messages.
     */
    public OutputWriter() {
        log = new ArrayList<>();
    }

    /**
     * Creates the file for writing logs. If the file already exists, it will be overwritten.
     *
     * @param filePath The path to the output file.
     * @throws IOException If an I/O error occurs during file creation.
     */
    public void createFile(String filePath) throws IOException {
        outputFile = new File(filePath);
        if (outputFile.exists()) {
            // If the file exists, clear it by overwriting
            new FileWriter(filePath, false).close();
        } else {
            // If the file does not exist, create it
            if (!outputFile.createNewFile()) {
                throw new IOException("Failed to create file: " + filePath);
            }
        }
    }

    /**
     * Logs a movement to a specific node.
     *
     * @param x The X-coordinate of the node.
     * @param y The Y-coordinate of the node.
     */
    public void logMovement(int x, int y) {
        log.add("Moving to " + x + "-" + y);
    }

    /**
     * Logs a message indicating that the current path is impassable.
     */
    public void logPathImpassable() {
        log.add("Path is impassable!");
    }

    /**
     * Logs a message when a specific objective is reached.
     *
     * @param objectiveNumber The number of the objective that has been reached.
     */
    public void logObjectiveReached(int objectiveNumber) {
        log.add("Objective " + objectiveNumber + " reached!");
    }

    /**
     * Logs the choice made when a wizard offers help.
     *
     * @param choice The number representing the wizard's choice.
     */
    public void logWizardChoice(int choice) {
        log.add("Number " + choice + " is chosen!");
    }

    /**
     * Writes all logged messages to the output file.
     * Assumes that {@code createFile} has already been called.
     *
     * @throws IOException If an I/O error occurs during writing.
     */
    public void writeToFile() throws IOException {
        if (outputFile == null) {
            throw new IllegalStateException("Output file has not been created. Call createFile() first.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String entry : log) {
                writer.write(entry);
                writer.newLine();
            }
        }
    }
}
