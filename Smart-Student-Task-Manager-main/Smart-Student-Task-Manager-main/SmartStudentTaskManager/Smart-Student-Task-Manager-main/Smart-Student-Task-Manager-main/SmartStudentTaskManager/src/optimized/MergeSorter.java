package optimized;

public class MergeSorter {

    public static Task[] sort(Task[] input, int count) {
        Task[] array = new Task[count];
        System.arraycopy(input, 0, array, 0, count);
        mergeSort(array, 0, count - 1);
        return array;
    }

    private static void mergeSort(Task[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) / 2;
        mergeSort(array, left, mid);
        mergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }

    private static void merge(Task[] array, int left, int mid, int right) {
        Task[] temp = new Task[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= right) {
            if (array[i].deadline <= array[j].deadline) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }

        while (j <= right) {
            temp[k++] = array[j++];
        }

        System.arraycopy(temp, 0, array, left, temp.length);
    }
}
