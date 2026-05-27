import baseline.BaselineSystem;
import java.util.Arrays;
import optimized.OptimizedSystem;

/**
 * BenchmarkAndPresentationDriver
 * 
 * This class runs comprehensive benchmarks comparing the Baseline and Optimized
 * task management systems. Both systems are tested under identical dataset sizes
 * to provide meaningful performance comparison.
 * 
 * IMPORTANT: This is a DSA implementation prototype. The system demonstrates
 * custom data structures (HashTable, MinHeap, MergeSorter) and graph-based
 * dependency management, but is primarily console/backend focused for now.
 * 
 * Systems Compared:
 * - BASELINE: Linear array, linear search, bubble sort, array shifting for deletion
 * - OPTIMIZED: Custom HashTable (open addressing), MinHeap, MergeSorter, dependency graph
 * 
 * Test Sizes: 100, 500, 1000 items
 */
public class BenchmarkAndPresentationDriver {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║        BASELINE vs OPTIMIZED SYSTEM PERFORMANCE ANALYSIS       ║");
        System.out.println("║                 Smart Student Task Manager                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        System.out.println("PROJECT SCOPE:");
        System.out.println("This system is a DSA implementation prototype focusing on custom");
        System.out.println("data structure implementations rather than a production task manager.\n");

        System.out.println("BASELINE APPROACH:");
        System.out.println("  • Task Storage: Fixed-size array with linear shifting");
        System.out.println("  • Search: O(n) linear scan");
        System.out.println("  • Sorting: Bubble sort O(n²)");
        System.out.println("  • Deletion: Array shifting O(n)\n");

        System.out.println("OPTIMIZED APPROACH:");
        System.out.println("  • Task Storage: Custom HashTable with open addressing & resizing");
        System.out.println("  • Search: O(1) average-case hash lookup");
        System.out.println("  • Sorting: Merge sort O(n log n)");
        System.out.println("  • Heap Priority: Custom MinHeap with swim/sink operations");
        System.out.println("  • Dependencies: Topological sort with in-degree tracking\n");

        System.out.println("OPERATIONS BENCHMARKED:");
        System.out.println("  1. Task insertion into storage structure");
        System.out.println("  2. Task lookup");
        System.out.println("  3. Sorting all tasks by deadline");
        System.out.println("  4. Heap-based urgent task selection (optimized only)\n");

        int[] testSizes = {100, 500, 1000};
        double[][] baselineResults = new double[testSizes.length][3];
        double[][] optimizedResults = new double[testSizes.length][3];

        System.out.println("═".repeat(70));
        System.out.println("BENCHMARK RESULTS");
        System.out.println("═".repeat(70));
        System.out.printf("%-12s | %-18s | %-18s | %-12s\n", "Dataset Size", "Baseline (ms)", "Optimized (ms)", "Improvement");
        System.out.println("─".repeat(70));

        for (int idx = 0; idx < testSizes.length; idx++) {
            int size = testSizes[idx];

            // Run multiple trials for accuracy
            double[] baselineTrials = new double[5];
            double[] optimizedTrials = new double[5];

            for (int trial = 0; trial < 5; trial++) {
                baselineTrials[trial] = BaselineSystem.evaluate(size);
                optimizedTrials[trial] = OptimizedSystem.evaluate(size);
            }

            // Use median to avoid outliers
            double baselineTime = median(baselineTrials);
            double optimizedTime = median(optimizedTrials);
            double improvement = ((baselineTime - optimizedTime) / baselineTime) * 100;

            baselineResults[idx][0] = size;
            baselineResults[idx][1] = baselineTime;
            optimizedResults[idx][0] = size;
            optimizedResults[idx][1] = optimizedTime;

            System.out.printf("%-12d | %-18.4f | %-18.4f | %+11.1f%%\n", 
                size, baselineTime, optimizedTime, improvement);
        }

        System.out.println("═".repeat(70));
        System.out.println("\nDETAILED ANALYSIS\n");

        System.out.println("BASELINE SYSTEM CHARACTERISTICS:");
        System.out.println("  • Pros: Simple, straightforward implementation");
        System.out.println("  • Cons: O(n) operations dominate; bubble sort is O(n²)");
        System.out.println("  • Best For: Small datasets, educational clarity\n");

        System.out.println("OPTIMIZED SYSTEM CHARACTERISTICS:");
        System.out.println("  • Pros: Custom structures optimized for task operations");
        System.out.println("  • Cons: More complex implementation, memory overhead");
        System.out.println("  • Best For: Larger datasets, performance-critical scenarios\n");

        System.out.println("═".repeat(70));
        System.out.println("IMPLEMENTATION NOTES");
        System.out.println("═".repeat(70));

        System.out.println("\n1. CUSTOM HASHTABLE (Optimized):");
        System.out.println("   • Uses open addressing with linear probing");
        System.out.println("   • Automatically resizes when load factor exceeds threshold");
        System.out.println("   • Task lookup: O(1) average, O(n) worst-case");

        System.out.println("\n2. MINHEAP (Optimized):");
        System.out.println("   • Full binary heap using array representation");
        System.out.println("   • Swim operation: bubbles new element up (O(log n))");
        System.out.println("   • Sink operation: bubbles root down (O(log n))");

        System.out.println("\n3. MERGESORT (Optimized):");
        System.out.println("   • Custom divide-and-conquer implementation");
        System.out.println("   • Guaranteed O(n log n) performance");
        System.out.println("   • Space complexity: O(n) for merging");

        System.out.println("\n4. DEPENDENCY GRAPH (Optimized):");
        System.out.println("   • Topological sort using Kahn's algorithm");
        System.out.println("   • Tracks task dependencies with in-degree counting");
        System.out.println("   • LIMITATION: Does not detect cycles (assumed acyclic)");

        System.out.println("\n" + "═".repeat(70));
        System.out.println("LIMITATIONS & FUTURE IMPROVEMENTS");
        System.out.println("═".repeat(70));

        System.out.println("\n1. DEPENDENCY HANDLING:");
        System.out.println("   • Current: Basic in-degree tracking");
        System.out.println("   • TODO: Add cycle detection using DFS or union-find");
        System.out.println("   • TODO: Validate against duplicate edges\n");

        System.out.println("2. SYSTEM ARCHITECTURE:");
        System.out.println("   • Current: Console-based DSA prototype");
        System.out.println("   • GUI: Separate Swing UI available (not in benchmark)");
        System.out.println("   • TODO: Full integration of UI with optimized backend\n");

        System.out.println("3. ERROR HANDLING:");
        System.out.println("   • Current: Minimal validation in core algorithms");
        System.out.println("   • TODO: Add comprehensive error handling\n");

        System.out.println("═".repeat(70));
        System.out.println("DEMONSTRATION SEQUENCE FOR PRESENTATION");
        System.out.println("═".repeat(70));
        System.out.println("\n1. Run this benchmark to show comparative performance");
        System.out.println("2. Showcase HashTable.search() with O(1) lookup");
        System.out.println("3. Extract minimum from MinHeap to demonstrate priority");
        System.out.println("4. Show DependencyGraph.topologicalSort() for task ordering");
        System.out.println("5. Visualize MergeSorter.sort() with sample data");
        System.out.println("6. (Optional) Launch GUI: BaselineTaskManagerGUI or TaskManagerGUI\n");

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    BENCHMARK COMPLETE                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Calculate median of an array to reduce impact of outliers
     */
    private static double median(double[] values) {
        double[] sorted = Arrays.copyOf(values, values.length);
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }
}
