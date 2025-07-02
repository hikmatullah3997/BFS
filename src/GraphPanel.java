import javax.swing.*; // For GUI components
import java.awt.*; // For drawing shapes and colors
import java.awt.event.*; // For handling mouse clicks
import java.util.*; // For using List, Map, Queue, etc.
import java.util.List; // Import List interface

// Represents a connection between two nodes
class Edge {
    Node from, to; // Starting and ending node of the edge
    Color color = Color.BLACK; // Default color of edge is black

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }
}

// Represents the whole graph with nodes and edges
class Graph {
    List<Node> nodes = new ArrayList<>(); // All nodes
    List<Edge> edges = new ArrayList<>(); // All edges
    Map<Node, List<Node>> adjList = new HashMap<>(); // Adjacency list for each node

    // Add a node and initialize its neighbor list
    public void addNode(Node node) {
        nodes.add(node);
        adjList.put(node, new ArrayList<>());
    }

    // Add an edge between two nodes and update their neighbor lists
    public void addEdge(Node a, Node b) {
        edges.add(new Edge(a, b));
        adjList.get(a).add(b);
        adjList.get(b).add(a);
    }

    // Breadth-First Search to find shortest path from start to end
    public List<Node> bfs(Node start, Node end) {
        Map<Node, Node> parentMap = new HashMap<>(); // Tracks parent of each node
        Queue<Node> queue = new LinkedList<>(); // Queue for BFS
        Set<Node> visited = new HashSet<>(); // Set of visited nodes

        queue.add(start);
        visited.add(start);
        parentMap.put(start, null); // Start has no parent

        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Get next node
            if (current == end) break; // Stop if reached end

            // Visit all neighbors
            for (Node neighbor : adjList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    parentMap.put(neighbor, current); // Track path
                }
            }
        }

        // Reconstruct and return the final path
        return reconstructPath(parentMap, end);
    }

    // Builds the path from start to end using the parent map
    private List<Node> reconstructPath(Map<Node, Node> parentMap, Node end) {
        LinkedList<Node> path = new LinkedList<>();
        Node current = end;

        while (current != null) {
            path.addFirst(current); // Add node to front of path
            current = parentMap.get(current); // Move to parent
        }

        return path;
    }

    // Reset all node and edge colors to default
    public void resetColors() {
        for (Node node : nodes) node.color = Color.BLUE;
        for (Edge edge : edges) edge.color = Color.BLACK;
    }
}

// This panel displays the graph and handles user interaction
public class GraphPanel extends JPanel {
    private Graph graph = new Graph(); // The graph instance
    private Node startNode, endNode; // Selected start and end nodes
    private List<Node> currentPath = new ArrayList<>(); // Stores BFS result path
    private Map<String, Node> nodeMap = new HashMap<>(); // Maps labels to nodes

    // Constructor: setup panel and graph
    public GraphPanel() {
        setPreferredSize(new Dimension(1000, 800)); // Set panel size
        setBackground(new Color(240, 248, 255)); // Light blue background
        createCustomGraph(); // Create all nodes and edges

        // Handle mouse clicks on nodes
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleNodeSelection(e.getPoint()); // Handle click
                repaint(); // Redraw the panel
            }
        });
    }

    // Create 13 nodes in a circle and connect them
    private void createCustomGraph() {
        int centerX = 500;
        int centerY = 400;
        int radius = 250;

        String[] labels = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"};
        for (int i = 0; i < 13; i++) {
            double angle = 2 * Math.PI * i / 13; // Calculate angle
            int x = centerX + (int) (radius * Math.cos(angle)); // X position
            int y = centerY + (int) (radius * Math.sin(angle)); // Y position
            Node node = new Node(labels[i], x, y); // Create node
            graph.addNode(node); // Add to graph
            nodeMap.put(labels[i], node); // Store in map
        }

        // Connect nodes by label
        connect("A", "B");
        connect("B", "C");
        connect("C", "D");
        connect("D", "E");
        connect("E", "A");
        connect("E", "B");
        connect("B", "D");
        connect("B", "F");
        connect("F", "G");
        connect("G", "A");
        connect("G", "H");
        connect("H", "D");
        connect("H", "B");
        connect("M", "A");
        connect("M", "B");
        connect("M", "C");
    }

    // Connect two nodes using their labels
    private void connect(String a, String b) {
        graph.addEdge(nodeMap.get(a), nodeMap.get(b));
    }

    // Handle user clicking on a node
    private void handleNodeSelection(Point point) {
        for (Node node : graph.nodes) {
            if (node.contains(point)) { // If click is inside node
                if (startNode == null) {
                    startNode = node;
                    node.color = Color.YELLOW; // First node is yellow
                } else if (endNode == null && node != startNode) {
                    endNode = node;
                    visualizeBFS(); // Run BFS
                } else {
                    resetGraph(); // Reset and start over
                    startNode = node;
                    node.color = Color.YELLOW;
                }
                break;
            }
        }
    }

    // Run BFS and color the path
    private void visualizeBFS() {
        graph.resetColors(); // Reset previous colors
        startNode.color = Color.YELLOW; // Start stays yellow

        if (startNode != null && endNode != null) {
            currentPath = graph.bfs(startNode, endNode); // Run BFS

            for (Node node : currentPath) node.color = Color.GREEN; // Color path nodes

            // Highlight edges in the path
            for (int i = 0; i < currentPath.size() - 1; i++) {
                Node current = currentPath.get(i);
                Node next = currentPath.get(i + 1);
                for (Edge edge : graph.edges) {
                    if ((edge.from == current && edge.to == next) ||
                            (edge.from == next && edge.to == current)) {
                        edge.color = Color.GREEN;
                        break;
                    }
                }
            }
        }
    }

    // Clear everything and reset the graph
    private void resetGraph() {
        graph.resetColors();
        startNode = null;
        endNode = null;
        currentPath.clear();
    }

    // Draw the graph on the screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth graphics

        // Draw all edges (lines)
        for (Edge edge : graph.edges) {
            g2d.setColor(edge.color);
            g2d.setStroke(new BasicStroke(edge.color == Color.GREEN ? 3 : 2));
            g2d.drawLine(edge.from.x, edge.from.y, edge.to.x, edge.to.y);
        }

        // Draw all nodes (circles)
        for (Node node : graph.nodes) {
            g2d.setColor(node.color);
            g2d.fillOval(node.x - Node.DIAMETER / 2, node.y - Node.DIAMETER / 2, Node.DIAMETER, Node.DIAMETER);

            g2d.setColor(Color.BLACK); // Border color
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(node.x - Node.DIAMETER / 2, node.y - Node.DIAMETER / 2, Node.DIAMETER, Node.DIAMETER);

            // Draw node label (A, B, etc.)
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(node.id);
            g2d.drawString(node.id, node.x - textWidth / 2, node.y + fm.getAscent() / 3);
        }

        // Draw instructions at the top-left
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        String instruction = startNode == null ? "Click start node" :
                endNode == null ? "Click end node" :
                        "Click any node to reset";
        g2d.drawString(instruction, 20, 30);
    }
}