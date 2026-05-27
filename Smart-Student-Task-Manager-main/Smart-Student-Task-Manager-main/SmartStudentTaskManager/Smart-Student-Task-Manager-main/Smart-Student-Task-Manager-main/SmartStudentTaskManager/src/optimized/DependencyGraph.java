package optimized;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class DependencyGraph {

    private final List<Integer>[] adj;
    private final int nodes;

    @SuppressWarnings("unchecked")
    public DependencyGraph(int nodes) {
        this.nodes = nodes;
        adj = new ArrayList[nodes + 1];
        for (int i = 0; i <= nodes; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addEdge(int from, int to) {
        adj[from].add(to);
    }

    public int[] topologicalSort() {
        int[] indegree = new int[nodes + 1];
        for (int i = 1; i <= nodes; i++) {
            for (int neighbor : adj[i]) {
                indegree[neighbor]++;
            }
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 1; i <= nodes; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.remove();
            order.add(current);
            for (int neighbor : adj[current]) {
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return order.stream().mapToInt(Integer::intValue).toArray();
    }
}
