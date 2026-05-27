import baseline.BaselineSystem;
import baseline.Task;
import optimized.OptimizedSystem;
import optimized.HashTable;
import optimized.MinHeap;
import optimized.MergeSorter;
import optimized.DependencyGraph;

/**
 * PresentationDemo - Live demonstration of key DSA features
 * 
 * This demo showcases:
 * 1. Hash table O(1) lookup vs baseline O(n) linear search
 * 2. MinHeap extraction for urgent task prioritization
 * 3. Topological sort for dependency resolution
 * 4. Merge sort for deadline-based scheduling
 * 5. Cycle detection in dependency graphs
 */
public class PresentationDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║             DSA PROTOTYPE SYSTEM - LIVE DEMONSTRATION           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        demo1_HashTableLookup();
        pause();

        demo2_MinHeapPriority();
        pause();

        demo3_TopologicalSort();
        pause();

        demo4_MergeSorting();
        pause();

        demo5_CycleDetection();
        pause();

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                   DEMONSTRATION COMPLETE                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    private static void demo1_HashTableLookup() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("DEMO 1: HashTable O(1) Lookup vs Linear Search O(n)");
        System.out.println("═".repeat(70));

        System.out.println("\nOPTIMIZED APPROACH: Custom HashTable with Linear Probing");
        HashTable table = new HashTable(100);
        optimized.Task t1 = new optimized.Task(1, "Math Assignment", 2, 5);
        optimized.Task t2 = new optimized.Task(2, "Programming Project", 1, 2);
        optimized.Task t3 = new optimized.Task(3, "Research Paper", 3, 7);

        System.out.println("Inserting 3 tasks...");
        table.insert(t1);
        table.insert(t2);
        table.insert(t3);
        System.out.println("✓ Insertion complete\n");

        System.out.println("Searching for Task ID 2:");
        long startOpt = System.nanoTime();
        optimized.Task found = table.search(2);
        long endOpt = System.nanoTime();
        if (found != null) {
            System.out.println("  Found: " + found.getName());
            System.out.println("  Time: " + (endOpt - startOpt) + " ns (O(1) average case)");
        }

        System.out.println("\nBASELINE APPROACH: Linear Array Scan");
        baseline.Task[] tasks = new baseline.Task[3];
        tasks[0] = new baseline.Task(1, "Math Assignment", 2, 5);
        tasks[1] = new baseline.Task(2, "Programming Project", 1, 2);
        tasks[2] = new baseline.Task(3, "Research Paper", 3, 7);

        System.out.println("Searching for Task ID 2 in array:");
        long startBase = System.nanoTime();
        baseline.Task foundBase = null;
        for (baseline.Task t : tasks) {
            if (t.getId() == 2) {
                foundBase = t;
                break;
            }
        }
        long endBase = System.nanoTime();
        if (foundBase != null) {
            System.out.println("  Found: " + foundBase.getName());
            System.out.println("  Time: " + (endBase - startBase) + " ns (O(n) linear scan)");
        }

        System.out.println("\n→ For small datasets: similar time");
        System.out.println("→ For 1M tasks: HashTable stays constant, linear search becomes 1M iterations");
    }

    private static void demo2_MinHeapPriority() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("DEMO 2: MinHeap Priority Selection (O(log n) extraction)");
        System.out.println("═".repeat(70));

        MinHeap heap = new MinHeap(10);

        System.out.println("\nAdding tasks by deadline priority (lower deadline = higher priority):");
        optimized.Task t1 = new optimized.Task(1, "Report Due", 1, 1);
        optimized.Task t2 = new optimized.Task(2, "Study for Exam", 2, 3);
        optimized.Task t3 = new optimized.Task(3, "Project Submission", 3, 2);
        optimized.Task t4 = new optimized.Task(4, "Read Chapter", 1, 5);

        System.out.println("  • Task 1 (Report Due) - Deadline: 1");
        heap.insert(t1);
        System.out.println("  • Task 2 (Study for Exam) - Deadline: 3");
        heap.insert(t2);
        System.out.println("  • Task 3 (Project Submission) - Deadline: 2");
        heap.insert(t3);
        System.out.println("  • Task 4 (Read Chapter) - Deadline: 5");
        heap.insert(t4);

        System.out.println("\nExtracting tasks in priority order (soonest deadline first):");
        int extractCount = 1;
        while (heap.size() > 0) {
            optimized.Task urgent = heap.extractMin();
            System.out.println("  " + extractCount + ". " + urgent.getName() + " (Deadline: " + urgent.getDeadline() + ")");
            extractCount++;
        }

        System.out.println("\n→ MinHeap ensures urgent tasks are always at root");
        System.out.println("→ Extract operation is O(log n) with automatic rebalancing");
    }

    private static void demo3_TopologicalSort() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("DEMO 3: Topological Sort for Task Dependencies");
        System.out.println("═".repeat(70));

        DependencyGraph graph = new DependencyGraph(5);

        System.out.println("\nDefining task dependencies (edges):");
        System.out.println("  Task 1 → Task 2 (must complete 1 before 2)");
        System.out.println("  Task 2 → Task 3");
        System.out.println("  Task 1 → Task 4");
        System.out.println("  Task 4 → Task 5");

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(1, 4);
        graph.addEdge(4, 5);

        System.out.println("\nChecking for cycles...");
        if (graph.hasCycle()) {
            System.out.println("  ⚠ Cycle detected! (Invalid dependency structure)");
        } else {
            System.out.println("  ✓ No cycles (valid DAG - Directed Acyclic Graph)");
        }

        System.out.println("\nTopological sort (valid execution order):");
        int[] order = graph.topologicalSort();
        for (int i = 0; i < order.length; i++) {
            System.out.println("  " + (i + 1) + ". Task " + order[i]);
        }

        System.out.println("\n→ Kahn's algorithm ensures all dependencies are respected");
        System.out.println("→ Time complexity: O(V+E) where V=tasks, E=dependencies");
    }

    private static void demo4_MergeSorting() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("DEMO 4: MergeSorter for Deadline-Based Scheduling");
        System.out.println("═".repeat(70));

        System.out.println("\nOriginal task list (unsorted):");
        optimized.Task[] tasks = {
            new optimized.Task(1, "Submit Report", 1, 7),
            new optimized.Task(2, "Study Math", 2, 2),
            new optimized.Task(3, "Code Project", 3, 5),
            new optimized.Task(4, "Read Article", 1, 1),
            new optimized.Task(5, "Group Meeting", 2, 3)
        };

        for (optimized.Task t : tasks) {
            System.out.printf("  [ID:%d] %s - Deadline: %d\n", t.getId(), t.getName(), t.getDeadline());
        }

        System.out.println("\nSorting by deadline using MergeSorter (O(n log n))...");
        long start = System.nanoTime();
        optimized.Task[] sorted = MergeSorter.sort(tasks, tasks.length);
        long end = System.nanoTime();

        System.out.println("Sorted result (earliest deadline first):");
        for (optimized.Task t : sorted) {
            System.out.printf("  [ID:%d] %s - Deadline: %d\n", t.getId(), t.getName(), t.getDeadline());
        }

        System.out.printf("\nSort time: %.4f ms\n", (end - start) / 1_000_000.0);
        System.out.println("→ Custom divide-and-conquer implementation");
        System.out.println("→ Guaranteed O(n log n) performance");
    }

    private static void demo5_CycleDetection() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("DEMO 5: Cycle Detection in Dependency Graph");
        System.out.println("═".repeat(70));

        // Valid graph (no cycle)
        System.out.println("\nGraph 1: Valid Dependency Structure");
        DependencyGraph validGraph = new DependencyGraph(3);
        System.out.println("  Task 1 → Task 2");
        System.out.println("  Task 2 → Task 3");
        validGraph.addEdge(1, 2);
        validGraph.addEdge(2, 3);

        System.out.println("  Cycle detected? " + (validGraph.hasCycle() ? "YES ✗" : "NO ✓"));

        // Invalid graph (has cycle)
        System.out.println("\nGraph 2: Invalid Circular Dependency");
        DependencyGraph invalidGraph = new DependencyGraph(3);
        System.out.println("  Task 1 → Task 2");
        System.out.println("  Task 2 → Task 3");
        System.out.println("  Task 3 → Task 1 (creates cycle!)");
        invalidGraph.addEdge(1, 2);
        invalidGraph.addEdge(2, 3);
        invalidGraph.addEdge(3, 1);

        System.out.println("  Cycle detected? " + (invalidGraph.hasCycle() ? "YES ✓" : "NO ✗"));

        System.out.println("\n→ DFS-based cycle detection uses O(V+E) time");
        System.out.println("→ Color states: WHITE=unvisited, GRAY=visiting, BLACK=finished");
        System.out.println("→ Back edge (gray→gray) indicates a cycle");
    }

    private static void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
