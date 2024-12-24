public class GraphCanvasGuide {

    public static String guide =
            "--- GraphCanvas v1.0.0 by Yuk ---\n" +
                    "=============================================================\n" +
                    "A simple visual graph editor with various built-in algorithms.\n" +
                    "\n" +
                    "Graph types:\n" +
                    "Does not allow loops.\n" +
                    "At most 1 edge between 2 vertices.\n" +
                    "- Undirected: Edges are bidirectional.\n" +
                    "- Directed: Edges are unidirectional (arrowheads).\n" +
                    "- Unweighted: Edges have no weight.\n" +
                    "- Weighted: Edges have a weight (only allows positive integer).\n" +
                    "\n" +
                    "Utilities:\n" +
                    "- Guide button: Show this guide.\n" +
                    "- Lock button: Disable editing in the canvas and enables use of the console.\n" +
                    "- Vertices, Edge, Weight, Delete buttons: Drawing Mode.\n" +
                    "- Reset button: Clear the canvas and create new graph type.\n" +
                    "- Quit button: Exit the program.\n" +
                    "\n" +
                    "Drawing Mode:\n" +
                    "The yellow-highlighted button is the current Mode.\n" +
                    "- Vertices Mode: Click to create 0-Indexed vertices.\n" +
                    "- Edge Mode: Click on a vertex to highlight it (red) and click on another vertex to create an edge between them.\n" +
                    "(For weighted graphs, edges are created with default weight 1).\n" +
                    "- Weight Mode (only for Weighted Graph): Click on an edge to edit its weight.\n" +
                    "- Delete Mode: Click on a vertex/edge to delete it.\n" +
                    "\n" +
                    "Console:\n" +
                    "- \"randw {max}\" : Randomizes weights (1 to max inclusive) of all edges for weighted graphs.\n" +
                    "- \"connectall\" : Connect all vertices.\n" +
                    "- \"bfs {from} {to}\" : Breadth First Search (display path and length).\n" +
                    "- \"dfs {from} {to}\" : Depth First Search (display path and length).\n" +
                    "- \"dijk {from} {to}\" : Dijkstra's Algorithm (display path and length).\n" +
                    "- \"fullbfs {start}\" : Full Breadth First Search (display all paths and lengths).\n" +
                    "- \"fulldfs {start}\" : Full Depth First Search (display all paths and lengths).\n" +
                    "- \"fulldijk {start}\" : Full Dijkstra's Algorithm (display all paths and lengths).";

}
