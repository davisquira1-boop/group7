package optimized;

public class MinHeap {

    private final Task[] heap;
    private int size = 0;

    public MinHeap(int capacity) {
        heap = new Task[capacity];
    }

    public void insert(Task task) {
        heap[size] = task;
        int current = size;
        while (current > 0 && heap[current].priority < heap[(current - 1) / 2].priority) {
            Task temp = heap[current];
            heap[current] = heap[(current - 1) / 2];
            heap[(current - 1) / 2] = temp;
            current = (current - 1) / 2;
        }
        size++;
    }

    public Task extractMin() {
        if (size == 0) {
            return null;
        }
        Task min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapify(0);
        return min;
    }

    public int size() {
        return size;
    }

    private void heapify(int i) {
        int smallest = i;
        int left = (2 * i) + 1;
        int right = (2 * i) + 2;
        if (left < size && heap[left].priority < heap[smallest].priority) {
            smallest = left;
        }
        if (right < size && heap[right].priority < heap[smallest].priority) {
            smallest = right;
        }
        if (smallest != i) {
            Task temp = heap[i];
            heap[i] = heap[smallest];
            heap[smallest] = temp;
            heapify(smallest);
        }
    }
}
