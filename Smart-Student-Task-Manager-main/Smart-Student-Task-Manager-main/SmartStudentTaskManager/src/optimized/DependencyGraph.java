package optimized;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * DependencyGraph - Manages task dependencies using directed graph representation.
 * 
 * Features:
 * - Adjacency list representation for sparse graphs
 * - Topological sort using Kahn's algorithm (O(V+E))
 * - Cycle detection using DFS
 * 
 * IMPORTANT: This implementation assumes tasks will be provided explicitly.
 * The graph size is determined at construction.
 */
public class DependencyGraph {

    private final List<Integer>[] adj;
    private final int nodes;
    private boolean hasCycle = false;

    @SuppressWarnings("unchecked")
    public DependencyGraph(int nodes) {
        this.nodes = nodes;
        adj = new ArrayList[nodes + 1];
        for (int i = 0; i <= nodes; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addEdge(int from, int to) {
        // NOTE: Does not validate duplicate edges or check bounds
        // In production, add: if (from < 1 || from > nodes || to < 1 || to > nodes) throw exception
        adj[from].add(to);
    }

    /**
     * Detects if the graph contains a cycle using DFS.
     * Uses color-based approach: WHITE=0 (unvisited), GRAY=1 (visiting), BLACK=2 (visited)
     * @return true if cycle detected, false otherwise
     */
    public boolean hasCycle() {
        int[] color = new int[nodes + 1]; // 0=white, 1=gray, 2=black
        for (int i = 1; i <= nodes; i++) {
            if (color[i] == 0) {
                if (dfsCycleDetect(i, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsCycleDetect(int node, int[] color) {
        color[node] = 1; // Mark as GRAY (currently visiting)
        for (int neighbor : adj[node]) {
            if (color[neighbor] == 1) {
                // Back edge found - cycle detected
                return true;
            }
            if (color[neighbor] == 0) {
                if (dfsCycleDetect(neighbor, color)) {
                    return true;
                }
            }
        }
        color[node] = 2; // Mark as BLACK (finished)
        return false;
    }

    /**
     * Kahn's Algorithm for Topological Sort
     * Time Complexity: O(V + E) where V=vertices, E=edges
     * 
     * IMPORTANT: This algorithm assumes the graph is acyclic (DAG).
     * If a cycle exists, not all nodes will be included in the result.
     * Check hasCycle() before calling this method in production code.
     * 
     * @return array of node IDs in topological order
     */
    public int[] topologicalSort() {
        int[] indegree = new int[nodes + 1];
        
        // Calculate in-degrees
        for (int i = 1; i <= nodes; i++) {
            for (int neighbor : adj[i]) {
                indegree[neighbor]++;
            }
        }

        // Initialize queue with all nodes having in-degree 0
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 1; i <= nodes; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        // Process nodes in topological order
        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.remove();
            order.add(current);
            
            // Reduce in-degree of neighbors
            for (int neighbor : adj[current]) {
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // WARNING: If order.size() < nodes, a cycle exists
        if (order.size() < nodes) {
            System.err.println("WARNING: Topological sort incomplete. Graph may contain a cycle.");
        }

        return order.stream().mapToInt(Integer::intValue).toArray();
    }
}
