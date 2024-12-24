import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GraphConsole extends JPanel {

    private JTextArea outputArea;
    private JTextField inputField;
    private GraphCanvas canvas;

    public GraphConsole(GraphCanvas canvas) {
        this.canvas = canvas;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        outputArea = new JTextArea(20, 30);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(300, 400));

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, inputField.getPreferredSize().height));
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCommand(inputField.getText());
                inputField.setText("");
            }
        });

        add(scrollPane);
        add(inputField);

        // Set console to inactive upon instantiation
        setLocked(false);
    }

    private void handleCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 1) {
            appendOutput("Invalid command");
            return;
        }
        String algorithm = parts[0];
        int start, end, max;
        try {
            switch (algorithm) {
                case "bfs":
                case "dfs":
                case "dijk":
                    if (parts.length < 3) {
                        appendOutput("Invalid arguments");
                        return;
                    }
                    start = Integer.parseInt(parts[1]);
                    end = Integer.parseInt(parts[2]);
                    if (start < 0 || start >= canvas.getGraph().getVertices().size() || end < 0 || end >= canvas.getGraph().getVertices().size()) {
                        appendOutput("Invalid");
                        return;
                    }
                    if (algorithm.equals("bfs")) {
                        runBFS(start, end);
                    } else if (algorithm.equals("dfs")) {
                        runDFS(start, end);
                    } else if (algorithm.equals("dijk")) {
                        runDijkstra(start, end);
                    }
                    break;
                case "randw":
                    if (parts.length < 2) {
                        appendOutput("Invalid arguments");
                        return;
                    }
                    max = Integer.parseInt(parts[1]);
                    if (!canvas.getGraph().isWeighted) {
                        appendOutput("Weighted is false");
                    } else {
                        canvas.getGraph().randomizeWeights(max);
                        canvas.repaint();
                        appendOutput("Weights randomized");
                        appendOutput(canvas.graphToString());
                    }
                    break;
                case "fullbfs":
                case "fulldfs":
                case "fulldijk":
                    if (parts.length < 2) {
                        appendOutput("Invalid arguments");
                        return;
                    }
                    start = Integer.parseInt(parts[1]);
                    if (start < 0 || start >= canvas.getGraph().getVertices().size()) {
                        appendOutput("Invalid start vertex");
                        return;
                    }
                    runFullAlgorithm(algorithm, start);
                    break;
                case "connectall":
                    canvas.getGraph().connectAllVertices();
                    canvas.repaint();
                    appendOutput("All vertices connected");
                    appendOutput(canvas.graphToString());
                    break;
                default:
                    appendOutput("Unknown command");
            }
        } catch (NumberFormatException e) {
            appendOutput("Invalid vertex id or max value");
        }
    }

    private void runFullAlgorithm(String algorithm, int start) {
        Graph graph = canvas.getGraph();
        appendOutput("Running \"" + algorithm + "\" from Vertex " + start + "...");
        for (int end = 0; end < graph.getVertices().size(); end++) {
            if (start == end) continue;
            switch (algorithm) {
                case "fullbfs":
                    runBFS(start, end);
                    break;
                case "fulldfs":
                    runDFS(start, end);
                    break;
                case "fulldijk":
                    runDijkstra(start, end);
                    break;
            }
        }
    }

    public void setLocked(boolean isLocked) {
        if (isLocked) {
            outputArea.setBackground(Color.WHITE);
            outputArea.setText("");
            inputField.setEnabled(true);
        } else {
            outputArea.setBackground(Color.LIGHT_GRAY);
            outputArea.setText("Canvas not Locked");
            inputField.setEnabled(false);
        }
    }

    private void runBFS(int start, int end) {
        appendOutput("Running BFS... (from " + start + " to " + end + ")");
        List<Integer> path = canvas.getGraph().bfs(start, end);
        if (path == null) {
            appendOutput("Unreachable\n");
        } else {
            appendOutput("Path: " + path);
            appendOutput("Length: " + calculatePathWeight(path) + "\n");
        }
    }

    private void runDFS(int start, int end) {
        appendOutput("Running DFS... (from " + start + " to " + end + ")");
        List<Integer> path = canvas.getGraph().dfs(start, end);
        if (path == null) {
            appendOutput("Unreachable\n");
        } else {
            appendOutput("Path: " + path);
            appendOutput("Length: " + calculatePathWeight(path) + "\n");
        }
    }

    private void runDijkstra(int start, int end) {
        appendOutput("Running Dijkstra's algorithm... (from " + start + " to " + end + ")");
        List<Integer> path = canvas.getGraph().dijkstra(start, end);
        if (path == null) {
            appendOutput("Unreachable\n");
        } else {
            appendOutput("Path: " + path);
            appendOutput("Length: " + calculatePathWeight(path) + "\n");
        }
    }

    private int calculatePathWeight(List<Integer> path) {
        int totalWeight = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);
            for (Edge edge : canvas.getGraph().getVertices().get(from).getEdges()) {
                if (edge.getTo().getId() == to) {
                    totalWeight += edge.getWeight();
                    break;
                }
            }
        }
        return totalWeight;
    }

    public void appendOutput(String text) {
        outputArea.append(text + "\n");
    }
}
