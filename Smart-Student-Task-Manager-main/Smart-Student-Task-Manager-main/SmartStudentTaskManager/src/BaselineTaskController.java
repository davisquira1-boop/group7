import baseline.BaselineSystem;
import baseline.Task;
import java.util.*;

public class BaselineTaskController {

    private final BaselineSystem system;
    // adjacency list for dependencies
    private final Map<Integer, List<Integer>> adj = new HashMap<>();
    private final Map<Integer, Integer> indegree = new HashMap<>();
    private final Map<Integer, Task> tasks = new HashMap<>();

    public BaselineTaskController(int initialCapacity) {
        this.system = new BaselineSystem();
    }

    public synchronized String addTask(int id, String name, int priority, int deadline) {
        if (tasks.containsKey(id)) {
            return "Task with ID " + id + " already exists.";
        }
        Task t = new Task(id, name, priority, deadline);
        tasks.put(id, t);
        system.addTask(t);
        adj.putIfAbsent(id, new ArrayList<>());
        indegree.putIfAbsent(id, 0);
        return "Added task: " + id + " - " + name;
    }

    public synchronized String addDependency(int from, int to) {
        if (!tasks.containsKey(from) || !tasks.containsKey(to)) {
            return "Both tasks must exist to add a dependency.";
        }
        adj.putIfAbsent(from, new ArrayList<>());
        adj.get(from).add(to);
        indegree.put(to, indegree.getOrDefault(to, 0) + 1);
        return "Added dependency: " + from + " -> " + to;
    }

    public synchronized String markDone(int id) {
        Task t = tasks.get(id);
        if (t == null) return "Task not found.";
        if (t.isDone()) return "Task already marked done.";
        t.setDone(true);
        return "Marked Task " + id + " as DONE.";
    }

    public synchronized List<Task> getUnlockedTasks() {
        // Kahn's algorithm considering done tasks
        Map<Integer, Integer> indeg = new HashMap<>(indegree);
        Queue<Integer> q = new ArrayDeque<>();
        for (int id : tasks.keySet()) {
            if (indeg.getOrDefault(id, 0) == 0 && !tasks.get(id).isDone()) q.add(id);
        }
        List<Task> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int cur = q.remove();
            if (!tasks.get(cur).isDone()) order.add(tasks.get(cur));
            for (int nb : adj.getOrDefault(cur, Collections.emptyList())) {
                indeg.put(nb, indeg.getOrDefault(nb, 0) - 1);
                if (indeg.get(nb) == 0 && !tasks.get(nb).isDone()) q.add(nb);
            }
        }
        return order;
    }

    public synchronized List<Task> getMasterSchedule() {
        List<Task> list = new ArrayList<>();
        for (Task t : tasks.values()) if (!t.isDone()) list.add(t);
        // Simple bubble sort by deadline
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).getDeadline() > list.get(j + 1).getDeadline()) {
                    Task temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }

    public synchronized Task lookup(int id) {
        return system.searchTask(id);
    }

    public synchronized String showAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL TASKS ===\n");
        List<Task> all = new ArrayList<>(tasks.values());
        all.sort(Comparator.comparingInt(a -> a.getId()));
        for (Task t : all) sb.append(formatTaskLine(t)).append('\n');
        return sb.toString();
    }

    private String formatTaskLine(Task t) {
        return String.format("[ID:%d] %s | Due in: %d | Priority: %d | Done: %s",
            t.getId(), t.getName(), t.getDeadline(), t.getPriority(), t.isDone() ? "YES" : "NO");
    }
}
