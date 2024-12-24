import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GraphCanvas extends JPanel {

    private Graph graph;
    private String mode;
    private Vertex selectedVertex;
    private JButton verticesButton;
    private JButton edgeButton;
    private JButton deleteButton;
    private JButton weightButton;
    private JButton lockButton;
    private JButton resetButton;
    private boolean isLocked;
    private boolean isDirected;
    private boolean isWeighted;
    private final GraphConsole console;

    public static JDialog guideDialog;

    public GraphCanvas() {
        promptGraphAttributes();
        graph = new Graph(isDirected, isWeighted, new ArrayList<>());
        mode = "Vertices";
        selectedVertex = null;
        isLocked = false;

        console = new GraphConsole(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isLocked) return;

                Point clickedPoint = e.getPoint();
                switch (mode) {
                    case "Vertices" -> {
                        Vertex vertex = new Vertex(graph.getVertices().size());
                        vertex.setPosition(clickedPoint);
                        graph.addVertex(vertex);
                        repaint();
                    }
                    case "Edge" -> handleEdgeMode(clickedPoint);
                    case "Delete" -> handleDeleteMode(clickedPoint);
                    case "Weight" -> handleWeightMode(clickedPoint);
                }
            }
        });

        setupUI();
    }

    private void promptGraphAttributes() {
        int directedOption = JOptionPane.showConfirmDialog(this, "Is the graph directed?", "Graph Type", JOptionPane.YES_NO_OPTION);
        isDirected = (directedOption == JOptionPane.YES_OPTION);

        int weightedOption = JOptionPane.showConfirmDialog(this, "Is the graph weighted?", "Graph Type", JOptionPane.YES_NO_OPTION);
        isWeighted = (weightedOption == JOptionPane.YES_OPTION);
    }

    private void handleEdgeMode(Point clickedPoint) {
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.getPosition().distance(clickedPoint) <= 10) {
                if (selectedVertex == null) {
                    selectedVertex = vertex;
                } else {
                    graph.addEdge(selectedVertex, vertex);
                    selectedVertex = null;
                }
                repaint();
                break;
            }
        }
    }

    private void handleDeleteMode(Point clickedPoint) {
        // Check if a vertex is clicked
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.getPosition().distance(clickedPoint) <= 10) {
                graph.removeVertex(vertex);
                repaint();
                return;
            }
        }

        // Check if an edge is clicked
        for (Edge edge : graph.getEdges()) {
            if (isPointOnLine(clickedPoint, edge.getFrom().getPosition(), edge.getTo().getPosition())) {
                graph.removeEdge(edge.getFrom(), edge.getTo());
                repaint();
                return;
            }
        }
    }

    private void handleWeightMode(Point clickedPoint) {
        for (Edge edge : graph.getEdges()) {
            if (isPointOnLine(clickedPoint, edge.getFrom().getPosition(), edge.getTo().getPosition())) {
                Integer newWeight = promptForWeight();
                graph.setWeight(edge, newWeight);
                repaint();
                return;
            }
        }
    }

    private Integer promptForWeight() {
        Integer weight = null;
        while (weight == null || weight <= 0 || weight > 99) {
            String input = JOptionPane.showInputDialog(this, "Enter weight (must be in range [1, 99]):");
            try {
                weight = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                weight = null;
            }
        }
        return weight;
    }

    private boolean isPointOnLine(Point p, Point start, Point end) {
        double distance = start.distance(end);
        double distanceToStart = p.distance(start);
        double distanceToEnd = p.distance(end);
        return Math.abs(distance - (distanceToStart + distanceToEnd)) < 1.0;
    }

    private void setupUI() {
        JFrame frame = new JFrame("GraphCanvas v1.0.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        setPreferredSize(new Dimension(800, 600));

        verticesButton = new JButton("Vertices");
        verticesButton.addActionListener(e -> setMode("Vertices"));

        edgeButton = new JButton("Edge");
        edgeButton.addActionListener(e -> setMode("Edge"));

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> setMode("Delete"));

        weightButton = new JButton("Weight");
        weightButton.addActionListener(e -> setMode("Weight"));
        weightButton.setEnabled(isWeighted);
        weightButton.setBackground(isWeighted ? null : Color.GRAY);

        lockButton = new JButton("Lock");
        lockButton.addActionListener(e -> toggleLock());

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> clearVertices());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        JButton guideButton = new JButton("Guide");
        guideButton.addActionListener(e -> showGuide());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(guideButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(verticesButton);
        buttonPanel.add(edgeButton);
        buttonPanel.add(weightButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(quitButton);

        frame.add(this, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(console, BorderLayout.EAST);
        frame.setVisible(true);

        updateButtonColors();
        updateButtonStates();
    }

    private void toggleLock() {
        isLocked = !isLocked;
        console.setLocked(isLocked);
        if (isLocked) {
            lockButton.setText("Unlock");
            mode = "";
            console.appendOutput("Unlock to use Canvas");
            console.appendOutput(graphToString());
        } else {
            lockButton.setText("Lock");
            setMode("Vertices");
        }
        updateButtonColors();
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean enabled = !isLocked;
        verticesButton.setEnabled(enabled);
        edgeButton.setEnabled(enabled);
        weightButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
        resetButton.setEnabled(enabled);
    }

    protected String graphToString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex vertex : graph.getVertices()) {
            sb.append(vertex.getId()).append(": ");
            for (Edge edge : vertex.getEdges()) {
                if (isDirected) {
                    sb.append("-> ");
                }
                sb.append(edge.getTo().getId());
                if (isWeighted && edge.getWeight() != null) {
                    sb.append(" (w: ").append(edge.getWeight()).append(")");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        sb.append("--------------------\n");
        return sb.toString();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw edges first
        g.setColor(Color.BLACK);
        for (Edge edge : graph.getEdges()) {
            drawEdge(g, edge);
            if (graph.isWeighted && edge.getWeight() != null) {
                g.setColor(Color.RED);
                g.setFont(g.getFont().deriveFont(14f)); // Increase font size
                Point midPoint = new Point((edge.getFrom().getPosition().x + edge.getTo().getPosition().x) / 2,
                        (edge.getFrom().getPosition().y + edge.getTo().getPosition().y) / 2);
                g.drawString(edge.getWeight().toString(), midPoint.x, midPoint.y);
                g.setFont(g.getFont().deriveFont(12f)); // Reset font size
                g.setColor(Color.BLACK);
            }
        }

        // Draw vertices on top of edges
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.equals(selectedVertex)) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillOval(vertex.getPosition().x - 10, vertex.getPosition().y - 10, 20, 20);
            g.setColor(Color.BLACK);
            g.drawOval(vertex.getPosition().x - 10, vertex.getPosition().y - 10, 20, 20);
            g.drawString(String.valueOf(vertex.getId()), vertex.getPosition().x - (vertex.getId() > 9 ? 6 : 3), vertex.getPosition().y + 4);
        }

        // Draw graph attributes
        g.setColor(Color.BLACK);
        g.drawString("Directed: " + graph.isDirected + ", Weighted: " + graph.isWeighted, 10, 20);
        g.drawString("V: " + graph.getNumVertex() + ", E: " + (graph.isDirected ? graph.getNumEdge() : graph.getNumEdge() / 2), 10, 40);
    }

    private void drawEdge(Graphics g, Edge edge) {
        Point from = edge.getFrom().getPosition();
        Point to = edge.getTo().getPosition();
        if (isDirected) {
            drawArrow(g, from, to);
        } else {
            g.drawLine(from.x, from.y, to.x, to.y);
        }
    }

    private void drawArrow(Graphics g, Point from, Point to) {
        int arrowSize = 10;
        int tipOffset = 10; // Offset for the arrow tip
        double angle = Math.atan2(to.y - from.y, to.x - from.x);

        int tipX = (int) (to.x - tipOffset * Math.cos(angle));
        int tipY = (int) (to.y - tipOffset * Math.sin(angle));

        int x1 = (int) (tipX - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (tipY - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (tipX - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (tipY - arrowSize * Math.sin(angle + Math.PI / 6));

        g.drawLine(from.x, from.y, tipX, tipY);

        int[] xPoints = {tipX, x1, x2};
        int[] yPoints = {tipY, y1, y2};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public void setMode(String mode) {
        if (isLocked) return;
        this.mode = mode;
        selectedVertex = null;
        updateButtonColors();
    }

    private void updateButtonColors() {
        verticesButton.setBackground(mode.equals("Vertices") ? Color.YELLOW : null);
        edgeButton.setBackground(mode.equals("Edge") ? Color.YELLOW : null);
        deleteButton.setBackground(mode.equals("Delete") ? Color.YELLOW : null);
        weightButton.setBackground(mode.equals("Weight") ? Color.YELLOW : null);
    }

    public void clearVertices() {
        if (isLocked) return;
        promptGraphAttributes();
        graph = new Graph(isDirected, isWeighted, new ArrayList<>());
        selectedVertex = null;
        weightButton.setEnabled(isWeighted);
        weightButton.setBackground(isWeighted ? null : Color.GRAY);
        repaint();
    }

    public Graph getGraph() {
        return graph;
    }

    private void showGuide() {
        if (guideDialog == null) {
            guideDialog = new JDialog();
            guideDialog.setTitle("Guide");
            guideDialog.setSize(400, 300);
            guideDialog.setLocationRelativeTo(null);
            guideDialog.setLayout(new BorderLayout());

            JTextArea guideText = new JTextArea(getGuide());
            guideText.setEditable(false);
            guideDialog.add(new JScrollPane(guideText), BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> guideDialog.dispose());
            guideDialog.add(closeButton, BorderLayout.SOUTH);
        }
        guideDialog.setVisible(true);
    }

    private String getGuide() {
        return GraphCanvasGuide.guide;
    }

    public static void main(String[] args) {
        new GraphCanvas();
    }
}