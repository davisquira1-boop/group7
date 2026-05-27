package optimized;

public class OptimizedSystem {

    private final HashTable hashTable = new HashTable(4096);
    private final MinHeap heap = new MinHeap(2000);
    private final DependencyGraph graph = new DependencyGraph(3);
    private final Task[] tasks = new Task[10];
    private int count = 0;

    public void run() {
        Task t1 = new Task(1, "Math Assignment", 5, 5);
        Task t2 = new Task(2, "Programming Project", 1, 2);
        Task t3 = new Task(3, "Research Paper", 3, 3);

        addTask(t1);
        addTask(t2);
        addTask(t3);

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        System.out.println("\n=== DEPENDENCY MAPPING ===");
        int[] order = graph.topologicalSort();
        System.out.println("Task order using topological sort:");
        for (int id : order) {
            Task current = hashTable.search(id);
            if (current != null) {
                current.display();
            }
        }

        System.out.println("\n=== HASH TABLE SEARCH ===");
        Task found = hashTable.search(2);
        if (found != null) {
            found.display();
        }

        System.out.println("\n=== MERGE SORTED TASK LIST ===");
        Task[] sorted = MergeSorter.sort(tasks, count);
        for (Task task : sorted) {
            task.display();
        }

        System.out.println("\n=== PRIORITY SCHEDULING WITH HEAP ===");
        Task urgent = heap.extractMin();
        if (urgent != null) {
            System.out.println("Most urgent task:");
            urgent.display();
        }
    }

    private void addTask(Task task) {
        tasks[count++] = task;
        hashTable.insert(task);
        heap.insert(task);
    }

    public static double evaluate(int size) {
        HashTable table = new HashTable(size * 2);
        MinHeap localHeap = new MinHeap(size);
        Task[] sample = new Task[size];

        for (int i = 0; i < size; i++) {
            sample[i] = new Task(i + 1, "Task " + (i + 1), (size - i) % 10 + 1, size - i);
        }

        long startTime = System.nanoTime();
        for (Task task : sample) {
            table.insert(task);
            localHeap.insert(task);
        }

        MergeSorter.sort(sample, size);
        table.search(size / 2);

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000.0;
    }
}
