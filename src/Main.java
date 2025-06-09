import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Ensure the correct number of arguments are provided
        if (args.length != 4) {
            System.err.println("Usage: java Main <input_file0> <input_file1> <input_file2> <output_file>");
            System.exit(1); // Exit the program with an error code
        }

        // Read file paths from command-line arguments
        String landFile = args[0];     // Input file 0
        String travelFile = args[1];   // Input file 1
        String missionFile = args[2];  // Input file 2
        String outputFile = args[3];   // Output file

        // Execute the program
        Execution execution = new Execution(landFile, travelFile, missionFile, outputFile);
        execution.execute();
    }
}
