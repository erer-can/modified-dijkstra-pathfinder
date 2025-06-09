import java.util.ArrayList;

/**
 * Represents a mission in the context of a game or simulation.
 * A mission includes:
 * <ul>
 *     <li>The target coordinates ({@code x}, {@code y}).</li>
 *     <li>A visibility radius defining the area of interest.</li>
 *     <li>Optional wizard-provided options to assist with the mission.</li>
 * </ul>
 */
public class Mission {
    private int x; // X-coordinate of the mission target
    private int y; // Y-coordinate of the mission target
    private int radius; // Visibility radius of the mission
    private ArrayList<Integer> wizardOptions; // List of wizard-provided options for assistance (can be null)

    /**
     * Constructs a {@code Mission} object.
     *
     * @param x             The x-coordinate of the mission target.
     * @param y             The y-coordinate of the mission target.
     * @param radius        The visibility radius of the mission.
     * @param wizardOptions An {@code ArrayList<Integer>} of wizard options for the mission.
     *                      This can be {@code null} if no options are provided.
     */
    public Mission(int x, int y, int radius, ArrayList<Integer> wizardOptions) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.wizardOptions = wizardOptions;
    }

    /**
     * Returns the x-coordinate of the mission target.
     *
     * @return The x-coordinate of the mission target.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the mission target.
     *
     * @return The y-coordinate of the mission target.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the visibility radius of the mission.
     *
     * @return The visibility radius of the mission.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Returns the list of wizard options available for the mission.
     *
     * @return An {@code ArrayList<Integer>} containing wizard options.
     *         Returns {@code null} if no options are provided.
     */
    public ArrayList<Integer> getWizardOptions() {
        return wizardOptions;
    }

    /**
     * Checks if the mission has any wizard options.
     *
     * @return {@code true} if wizard options are available, {@code false} otherwise.
     */
    public boolean hasWizardOptions() {
        return wizardOptions != null && !wizardOptions.isEmpty();
    }

    /**
     * Returns a string representation of the mission.
     * This includes the coordinates, radius, and wizard options (if any).
     *
     * @return A string describing the mission.
     */
    @Override
    public String toString() {
        return "Mission{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", wizardOptions=" + wizardOptions +
                '}';
    }
}
