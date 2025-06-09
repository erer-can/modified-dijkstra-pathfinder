/**
 * Represents a single node in the grid or graph.
 * A node is defined by:
 * <ul>
 *     <li>X and Y coordinates.</li>
 *     <li>A color that determines its type:
 *         <ul>
 *             <li>0 - Passable.</li>
 *             <li>1 - Blocked.</li>
 *             <li>≥2 - Hidden.</li>
 *         </ul>
 *     </li>
 *     <li>A flag indicating whether the node is revealed.</li>
 * </ul>
 */
public class Node {
    private int x; // X-coordinate of the node
    private int y; // Y-coordinate of the node
    private int color; // Color/type of the node (e.g., passable, blocked, hidden)
    private boolean isRevealed; // Indicates whether the node is revealed

    /**
     * Constructs a {@code Node} object.
     *
     * @param x     X-coordinate of the node.
     * @param y     Y-coordinate of the node.
     * @param color Color/type of the node.
     *              <ul>
     *                  <li>0 - Passable.</li>
     *                  <li>1 - Blocked.</li>
     *                  <li>≥2 - Hidden or special types.</li>
     *              </ul>
     */
    public Node(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.isRevealed = false; // By default, nodes are not revealed
    }

    /**
     * Returns the X-coordinate of the node.
     *
     * @return The X-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the Y-coordinate of the node.
     *
     * @return The Y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the color of the node.
     * If the node is not revealed, the return value depends on its type:
     * <ul>
     *     <li>1 - Blocked nodes are always reported as blocked.</li>
     *     <li>Other types are reported as passable (0).</li>
     * </ul>
     *
     * @return The color of the node.
     */
    public int getColor() {
        if (this.isRevealed) {
            return color;
        } else if (color == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the original color of the node, regardless of its revealed state.
     *
     * @return The original color of the node.
     */
    public int getOriginalColor() {
        return color;
    }

    /**
     * Sets the color of the node.
     *
     * @param color The new color/type of the node.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Checks if the node is revealed.
     *
     * @return {@code true} if the node is revealed, {@code false} otherwise.
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Marks the node as revealed.
     */
    public void reveal() {
        this.isRevealed = true;
    }

    /**
     * Returns a string representation of the node.
     *
     * @return A string describing the node's coordinates, type, and revealed state.
     */
    @Override
    public String toString() {
        return String.format("Node(%d, %d, Type: %d, Revealed: %b)", x, y, this.color, this.isRevealed);
    }
}
