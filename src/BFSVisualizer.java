import javax.swing.*; // For creating GUI (window, panels, labels)
import java.awt.*;    // For layout and colors

public class BFSVisualizer extends JFrame {
    
    // Constructor: sets up the main window
    public BFSVisualizer() {
        setTitle("BFS Pathfinding Visualizer"); // Title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app when window is closed
        setLayout(new BorderLayout()); // Layout style for placing components

        // Create and add the graph drawing area
        GraphPanel graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER); // Add to center of window

        // Create and style the footer label at the bottom
        JLabel footer = new JLabel("Breadth-First Search Visualization • Path shown in green", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.ITALIC, 14)); // Set font style
        footer.setForeground(new Color(70, 70, 70)); // Text color
        footer.setBackground(new Color(220, 230, 240)); // Background color
        footer.setOpaque(true); // Make background visible
        add(footer, BorderLayout.SOUTH); // Add to bottom of the window

        pack(); // Automatically adjust window size
        setLocationRelativeTo(null); // Center the window on screen
    }

    // Main method: runs the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BFSVisualizer().setVisible(true); // Show the window
        });
    }
}