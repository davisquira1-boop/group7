package baseline;

public class Task {

    int id;
    String name;
    int priority;
    int deadline;
    boolean done = false;

    public Task(int id, String name, int priority, int deadline) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.deadline = deadline;
    }

    public void display() {
        System.out.println(
                "ID: " + id +
                " | Task: " + name +
                " | Priority: " + priority +
                " | Deadline: " + deadline
        );
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPriority() { return priority; }
    public int getDeadline() { return deadline; }
}