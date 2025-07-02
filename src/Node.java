import java.awt.*; // Import for graphics like color and points

public class Node {
    public String id; // Name or label of the node
    public int x, y; // Position of the node on the screen
    public Color color = Color.BLUE; // Color of the node (default is blue)
    public static final int DIAMETER = 50; // Size of the node (circle)

    // Constructor to set the node's label and position
    public Node(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    // This checks if a mouse click (or point) is inside the circle
    public boolean contains(Point p) {
        // Using distance formula to check if point is inside the circle
        return Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2) <= Math.pow(DIAMETER / 2, 2);
    }
}