import java.util.Objects;

public class Edge implements Comparable<Edge> {

    private final Vertex from;
    private final Vertex to;
    private Integer weight;
    private boolean visited;
    private boolean highlighted;

    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
        this.weight = null;
        this.visited = false;
        this.highlighted = false;
    }

    public Edge(Vertex from, Vertex to, Integer weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.visited = false;
        this.highlighted = false;
    }

    @Override
    public int compareTo(Edge o) {
        return Integer.compare(weight, o.weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from.getId() +
                ", to=" + to.getId() +
                ", weight=" + weight +
                ", visited=" + visited +
                ", highlighted=" + highlighted +
                '}';
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}