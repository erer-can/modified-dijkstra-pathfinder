# Modified Dijkstra – Shortest Path on a Magical Map

**Course:** CMPE250 – Data Structures & Algorithms  
**Institution:** Boğaziçi University  

---

## Project Overview

This application computes and visualizes the shortest paths on a special “Magical Map” using a customized variant of Dijkstra’s algorithm. Beyond standard pathfinding, this version integrates mission-specific constraints and dynamic map transformations such as warp portals and terrain modifiers.

Key features:
- Parsing a complex map definition with special tiles and portals.
- Constructing a weighted graph representation reflecting magical effects.
- Running a modified Dijkstra algorithm to accommodate mission rules.
- Outputting both console results and graphical/structured outputs.
- Automated validation against expected outputs.

---

## Repository Structure

```
modified-dijkstra-pathfinder/
├── src/
│   ├── Main.java             # Entry point: orchestrates parsing, execution, and output
│   ├── MagicalMap.pdf        # Project specification with map rules and examples
│   ├── FileParser.java       # Reads map, node, and edge definitions
│   ├── FileParserResult.java # Holds parsed map, nodes, edges, and mission data
│   ├── Graph.java            # Builds the graph representation from parsed data
│   ├── Node.java             # Represents a vertex on the magical map with coordinates
│   ├── MyHashMap.java        # Custom hash map for mapping nodes and missions
│   ├── MyMinHeap.java        # Indexed min-heap for efficient priority queue operations
│   ├── Dijkstra.java         # Implements the modified Dijkstra’s algorithm
│   ├── DijkstraResult.java   # Contains the result set of distances and paths
│   ├── Mission.java          # Encapsulates mission constraints and target nodes
│   ├── Execution.java        # Coordinates algorithm execution and result formatting
│   ├── OutputWriter.java     # Writes the computed paths and metrics to output files
│   ├── FileComparator.java   # Utility for comparing actual vs. expected outputs
├── test-cases/
│   ├── case1/
│   │   ├── nodes.txt         # List of node coordinates and types
│   │   ├── edges.txt         # List of edges with travel times between nodes
│   │   ├── missions.txt      # Mission definitions with start, target, and objectives
│   │   └── output.txt        # Expected output for case1
│   ├── case2/
│   │   ├── nodes.txt
│   │   ├── edges.txt
│   │   ├── missions.txt
│   │   └── output.txt
│   └── ...                   # Additional test case folders (case3/, case4/, etc.)
├── README.md                 # Project documentation (this file)
├── .gitignore
└── LICENSE
```

---

## Core Components

### File Parsing
- **FileParser** and **FileParserResult** handle reading and validating map layouts, tile properties, and mission goals.  
- Supports special tile types: obstacles, portals, terrain with variable travel cost.

### Graph Construction
- **Graph** constructs an adjacency list where each **Node** holds neighbors and weights reflecting magical effects.  
- Uses **MyHashMap** to map coordinate keys to node objects for O(1) lookups.

### Priority Queue
- **MyMinHeap** provides an indexed priority queue supporting decrease-key operations essential for efficient Dijkstra performance (O(log n) per update).

### Modified Dijkstra Algorithm
- **Dijkstra** extends the classic algorithm to handle special cases:
  - Portals: edges with zero or negative weights that teleport between nodes.  
  - Terrain modifiers: dynamic weight adjustments based on mission parameters.
- Records distances and predecessor links in **DijkstraResult** for path reconstruction.

### Mission Constraints
- **Mission** encapsulates start/end points, mandatory waypoints, and cost limits.  
- Execution logic applies these constraints to prune or adjust paths during search.

### Execution Flow
- **Execution** class coordinates:
  1. Parsing input files.
  2. Building graph.
  3. Executing Dijkstra for each mission.
  4. Writing results via **OutputWriter**.

### Output and Validation
- **OutputWriter** formats the path sequences and cost metrics as specified.  
- **FileComparator** automates testing by comparing actual outputs to expected files in `outputs/`.

---

## How to Build and Run

1. **Compile** all source files:
   ```bash
   javac src/*.java
   ```
2. **Run** the application with map and mission inputs:
   ```bash
   java -cp src Main inputs/nodes.txt inputs/edges.txt inputs/missions.txt outputs/result1.txt
   ```
3. **Validate** against expected results:
   ```bash
   java -cp src FileComparator outputs/result1.txt outputs/expected_result1.txt
   ```

---

## License

This project is licensed under the MIT License. Please see the `LICENSE` file for details.
