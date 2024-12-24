# GraphCanvas v1.0.0 by Yuk
A simple visual graph editor with various built-in algorithms.

### Graph types:
Does not allow loops.
At most 1 edge between 2 vertices.
- Undirected: Edges are bidirectional.
- Directed: Edges are unidirectional (arrowheads).
- Unweighted: Edges have no weight.
- Weighted: Edges have a weight (only allows positive integer).

### Utilities:
- Guide button: Show this guide.
- Lock button: Disable editing in the canvas and enables use of the console.
- Vertices, Edge, Weight, Delete buttons: Drawing Mode.
- Reset button: Clear the canvas and create new graph type.
- Quit button: Exit the program.

### Drawing Mode:
The yellow-highlighted button is the current Mode.
- Vertices Mode: Click to create 0-Indexed vertices.
- Edge Mode: Click on a vertex to highlight it (red) and click on another vertex to create an edge between them.
(For weighted graphs, edges are created with default weight 1).
- Weight Mode (only for Weighted Graph): Click on an edge to edit its weight.
- Delete Mode: Click on a vertex/edge to delete it.

### Console:
- "randw {max}" : Randomizes weights (1 to max inclusive) of all edges for weighted graphs.
- "connectall" : Connect all vertices.
- "bfs {from} {to}" : Breadth First Search (display path and length).
- "dfs {from} {to}" : Depth First Search (display path and length).
- "dijk {from} {to}" : Dijkstra's Algorithm (display path and length).
- "fullbfs {start}" : Full Breadth First Search (display all paths and lengths).
- "fulldfs {start}" : Full Depth First Search (display all paths and lengths).
- "fulldijk {start}" : Full Dijkstra's Algorithm (display all paths and lengths).
