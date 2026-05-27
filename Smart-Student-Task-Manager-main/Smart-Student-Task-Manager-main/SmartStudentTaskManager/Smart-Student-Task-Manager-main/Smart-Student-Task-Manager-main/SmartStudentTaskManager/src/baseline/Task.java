package baseline;

public class Task {

    int id;
    String name;
    int deadline;

    public Task(int id, String name, int deadline) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
    }

    public void display() {
        System.out.println(
                "ID: " + id +
                " | Task: " + name +
                " | Deadline: " + deadline
        );
    }
}