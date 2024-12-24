import java.awt.Point;
import java.util.ArrayList;
import java.util.Objects;

public class Vertex implements Comparable<Vertex> {

    private int id;
    private final ArrayList<Edge> edges;
    private boolean visited;
    private boolean highlighted;
    private Point position;

    public Vertex(int id) {
        this.id = id;
        this.edges = new ArrayList<>();
        this.visited = false;
        this.highlighted = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
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

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                ", edges=" + edges +
                ", visited=" + visited +
                ", highlighted=" + highlighted +
                ", position=" + position +
                '}';
    }

    @Override
    public int compareTo(Vertex o) {
        return Integer.compare(id, o.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}