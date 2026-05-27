package baseline;

public class BaselineSystem {

    private final Task[] tasks = new Task[2000];
    private int count = 0;

    public void addTask(Task task) {
        tasks[count++] = task;
    }

    public Task searchTask(int id) {
        for (int i = 0; i < count; i++) {
            if (tasks[i].id == id) {
                return tasks[i];
            }
        }
        return null;
    }

    public boolean deleteTask(int id) {
        for (int i = 0; i < count; i++) {
            if (tasks[i].id == id) {
                for (int j = i; j < count - 1; j++) {
                    tasks[j] = tasks[j + 1];
                }
                tasks[--count] = null;
                return true;
            }
        }
        return false;
    }

    public Task getMostUrgentTask() {
        if (count == 0) {
            return null;
        }

        Task urgent = tasks[0];
        for (int i = 1; i < count; i++) {
            if (tasks[i].deadline < urgent.deadline) {
                urgent = tasks[i];
            }
        }
        return urgent;
    }

    public void bubbleSortByDeadline() {
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (tasks[j].deadline > tasks[j + 1].deadline) {
                    Task temp = tasks[j];
                    tasks[j] = tasks[j + 1];
                    tasks[j + 1] = temp;
                }
            }
        }
    }

    public void displayTasks() {
        System.out.println("\n=== BASELINE TASK LIST ===");
        for (int i = 0; i < count; i++) {
            tasks[i].display();
        }
    }

    public void run() {
        addTask(new Task(1, "Math Assignment", 5));
        addTask(new Task(2, "Programming Project", 2));
        addTask(new Task(3, "Research Paper", 1));

        displayTasks();

        System.out.println("\n=== BASELINE SEARCH ===");
        Task found = searchTask(2);
        if (found != null) {
            found.display();
        } else {
            System.out.println("Task not found.");
        }

        System.out.println("\n=== BASELINE SORT (Bubble Sort by Deadline) ===");
        bubbleSortByDeadline();
        displayTasks();

        System.out.println("\n=== BASELINE PRIORITY SCHEDULING ===");
        Task urgent = getMostUrgentTask();
        if (urgent != null) {
            System.out.println("Most urgent task:");
            urgent.display();
        }

        System.out.println("\n=== BASELINE DELETE / COMPLETE ===");
        if (deleteTask(2)) {
            System.out.println("Removed Task ID 2 and reshaped the array.");
        }
        displayTasks();
    }

    public static double evaluate(int size) {
        BaselineSystem baseline = new BaselineSystem();

        long startTime = System.nanoTime();
        for (int i = 1; i <= size; i++) {
            baseline.addTask(new Task(i, "Task " + i, size - i + 1));
        }

        baseline.bubbleSortByDeadline();
        baseline.searchTask(size / 2);

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000.0;
    }
}
