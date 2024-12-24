import java.util.*;

public class Graph {

    protected boolean isDirected;
    protected boolean isWeighted;
    protected ArrayList<Vertex> vertices;

    public Graph(boolean isDirected, boolean isWeighted, ArrayList<Vertex> vertices) {
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
        this.vertices = vertices;
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
        reindexVertices();
    }

    public void connectAllVertices() {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                Vertex from = vertices.get(i);
                Vertex to = vertices.get(j);
                addEdge(from, to);
            }
        }
    }

    public void removeVertex(Vertex vertex) {
        vertices.remove(vertex);
        for (Vertex v : vertices) {
            v.getEdges().removeIf(edge -> edge.getFrom().equals(vertex) || edge.getTo().equals(vertex));
        }
        reindexVertices();
    }

    public Edge findReverseEdge(Edge edge) {
        for (Vertex vertex : vertices) {
            for (Edge e : vertex.getEdges()) {
                if (e.getFrom().equals(edge.getTo()) && e.getTo().equals(edge.getFrom())) {
                    return e;
                }
            }
        }
        return null;
    }

    public void setWeight(Edge edge, Integer weight) {
        if (getEdges().contains(edge)) {
            Edge target = getEdge(edge);
            if (target != null) {
                target.setWeight(weight);
                Edge reverse = findReverseEdge(target);
                if (reverse != null) {
                    reverse.setWeight(weight);
                }
            }
        }
    }

    public void addEdge(Vertex from, Vertex to) {
        if (from.equals(to)) {
            // Prevent loops
            return;
        }

        if (from.getEdges().stream().anyMatch(edge -> edge.getTo().equals(to))) {
            // Prevent more than 1 edge between two vertices
            return;
        }
        if (to.getEdges().stream().anyMatch(edge -> edge.getTo().equals(from))) {
            // Prevent more than 1 edge between two vertices
            return;
        }

        int defaultWeight = isWeighted ? 1 : 0;
        Edge edge = new Edge(from, to, defaultWeight);
        from.addEdge(edge);
        if (!isDirected) {
            to.addEdge(new Edge(to, from, defaultWeight));
        }
    }

    public void removeEdge(Vertex from, Vertex to) {
        from.getEdges().removeIf(edge -> edge.getTo().equals(to));
        if (!isDirected) {
            to.getEdges().removeIf(edge -> edge.getTo().equals(from));
        }
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (Vertex vertex : vertices) {
            edges.addAll(vertex.getEdges());
        }
        return edges;
    }

    public Edge getEdge(Edge target) {
        for (Edge edge : getEdges()) {
            if (edge.equals(target)) {
                return edge;
            }
        }
        return null;
    }

    public void printAdjacencyList() {
        for (Vertex vertex : vertices) {
            System.out.print(vertex.getId() + ": ");
            for (Edge edge : vertex.getEdges()) {
                if (isDirected) {
                    System.out.print("-> ");
                }
                System.out.print(edge.getTo().getId());
                if (isWeighted && edge.getWeight() != null) {
                    System.out.print(" (weight: " + edge.getWeight() + ")");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    private void reindexVertices() {
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).setId(i);
        }
    }

    public List<Integer> bfs(int start, int end) {
        Queue<Vertex> queue = new LinkedList<>();
        boolean[] visited = new boolean[vertices.size()];
        int[] parent = new int[vertices.size()];
        Arrays.fill(parent, -1);

        queue.add(vertices.get(start));
        visited[start] = true;

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            int u = current.getId();

            if (u == end) {
                List<Integer> path = new ArrayList<>();
                for (int at = end; at != -1; at = parent[at]) {
                    path.add(at);
                }
                Collections.reverse(path);
                return path;
            }

            for (Edge edge : current.getEdges()) {
                int v = edge.getTo().getId();
                if (!visited[v]) {
                    queue.add(vertices.get(v));
                    visited[v] = true;
                    parent[v] = u;
                }
            }
        }
        return null; // Unreachable
    }

    public List<Integer> dfs(int start, int end) {
        Stack<Vertex> stack = new Stack<>();
        boolean[] visited = new boolean[vertices.size()];
        int[] parent = new int[vertices.size()];
        Arrays.fill(parent, -1);

        stack.push(vertices.get(start));

        while (!stack.isEmpty()) {
            Vertex current = stack.pop();
            int u = current.getId();

            if (visited[u]) continue;
            visited[u] = true;

            if (u == end) {
                List<Integer> path = new ArrayList<>();
                for (int at = end; at != -1; at = parent[at]) {
                    path.add(at);
                }
                Collections.reverse(path);
                return path;
            }

            List<Edge> edges = new ArrayList<>(current.getEdges());
            edges.sort(Comparator.comparingInt(e -> e.getTo().getId()));
            for (Edge edge : edges) {
                int v = edge.getTo().getId();
                if (!visited[v]) {
                    stack.push(vertices.get(v));
                    parent[v] = u;
                }
            }
        }
        return null; // Unreachable
    }

    public List<Integer> dijkstra(int start, int end) {
        int n = vertices.size();
        int[] dist = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[start] = 0;

        PriorityQueue<Vertex> pq = new PriorityQueue<>(Comparator.comparingInt(v -> dist[v.getId()]));
        pq.add(vertices.get(start));

        while (!pq.isEmpty()) {
            Vertex current = pq.poll();
            int u = current.getId();
            if (visited[u]) continue;
            visited[u] = true;

            for (Edge edge : current.getEdges()) {
                int v = edge.getTo().getId();
                int weight = edge.getWeight();
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    pq.add(vertices.get(v));
                }
            }
        }

        if (dist[end] == Integer.MAX_VALUE) {
            return null; // Unreachable
        }

        List<Integer> path = new ArrayList<>();
        for (int at = end; at != -1; at = parent[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public void randomizeWeights(int max) {
        if (!isWeighted) {
            return; // No weights to randomize in an unweighted graph
        }

        Random rand = new Random();
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.getEdges()) {
                setWeight(edge, rand.nextInt(max) + 1);
            }
        }
    }

    public int getNumVertex() {
        return vertices.size();
    }

    public int getNumEdge() {
        return getEdges().size();
    }
}