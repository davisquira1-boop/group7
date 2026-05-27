package optimized;

public class HashTable {

    private final Task[] table;

    public HashTable(int capacity) {
        table = new Task[capacity];
    }

    private int hash(int id) {
        return Math.abs(id) % table.length;
    }

    public void insert(Task task) {
        int index = hash(task.id);
        while (table[index] != null && table[index].id != task.id) {
            index = (index + 1) % table.length;
        }
        table[index] = task;
    }

    public Task search(int id) {
        int index = hash(id);
        int start = index;
        while (table[index] != null) {
            if (table[index].id == id) {
                return table[index];
            }
            index = (index + 1) % table.length;
            if (index == start) {
                break;
            }
        }
        return null;
    }
}
