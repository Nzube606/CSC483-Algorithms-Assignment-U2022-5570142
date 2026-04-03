package Sorting;

/**
 * Provides implementations of three classic sorting algorithms:
 * Insertion Sort, Merge Sort, and Quick Sort.
 *
 * <p>This class also maintains static counters to track the number of
 * element comparisons and data movements (swaps/assignments) performed
 * during each sorting operation. These counters are used by
 * {@link SortingExperiment} for empirical performance analysis.</p>
 *
 * <p>All sorting methods modify the input array in-place.</p>
 *
 * @author Ilodigwe Nzube
 * @version 1.0
 */
public class SortingAlgorithms {

    /** Total number of element comparisons in the most recent sort operation */
    private static long comparisons = 0;

    /** Total number of swaps or assignments in the most recent sort operation */
    private static long swaps = 0;

    /**
     * Returns the number of comparisons made during the most recent sorting operation.
     *
     * @return the total number of element comparisons
     */
    public static long getComparisons() {
        return comparisons;
    }

    /**
     * Returns the number of swaps or assignments made during the most recent sorting operation.
     *
     * @return the total number of swaps/assignments
     */
    public static long getSwaps() {
        return swaps;
    }

    /**
     * Resets the comparison and swap counters to zero.
     *
     * <p>This method must be called before each new sorting run to ensure
     * accurate and independent measurements for statistical analysis.</p>
     */
    public static void resetCounters() {
        comparisons = 0;
        swaps = 0;
    }

    /**
     * Sorts the specified array using Insertion Sort algorithm.
     *
     * <p>Insertion Sort builds the sorted array one element at a time by
     * inserting each element into its correct position in the already sorted
     * portion of the array.</p>
     *
     * <p>Time Complexity:
     * <ul>
     *   <li>Best case: O(n) — when the array is already sorted</li>
     *   <li>Average and Worst case: O(n²)</li>
     * </ul>
     * </p>
     *
     * @param arr the array to be sorted in ascending order
     */
    public static void insertionSort(int[] arr) {
        resetCounters();
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0) {
                comparisons++;
                if (arr[j] > key) {
                    swaps++;
                    arr[j + 1] = arr[j];
                    j--;
                } else {
                    break;
                }
            }

            arr[j + 1] = key;
            swaps++;
        }
    }

    /**
     * Sorts the specified array using Merge Sort algorithm (recursive implementation).
     *
     * <p>Merge Sort is a stable, divide-and-conquer sorting algorithm that
     * guarantees O(n log n) time complexity in all cases (best, average, and worst).</p>
     *
     * @param arr   the array to be sorted
     * @param left  the starting index of the subarray
     * @param right the ending index of the subarray
     */
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    /**
     * Merges two adjacent sorted sub-arrays into a single sorted sub-array.
     *
     * <p>This helper method is used internally by {@link #mergeSort(int[], int, int)}.</p>
     *
     * @param arr   the original array
     * @param left  starting index of the left subarray
     * @param mid   ending index of the left subarray
     * @param right ending index of the right subarray
     */
    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= right) {
            comparisons++;
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
            swaps++;
        }

        while (i <= mid) {
            temp[k++] = arr[i++];
            swaps++;
        }

        while (j <= right) {
            temp[k++] = arr[j++];
            swaps++;
        }

        for (i = left, k = 0; i <= right; i++, k++) {
            arr[i] = temp[k];
            swaps++;
        }
    }

    /**
     * Sorts the specified array using Quick Sort algorithm (recursive).
     *
     * <p>Quick Sort is an in-place sorting algorithm with average time complexity
     * of O(n log n). This implementation uses random pivot selection to reduce
     * the chance of worst-case O(n²) performance on sorted or reverse-sorted data.</p>
     *
     * @param arr  the array to be sorted
     * @param low  the starting index of the subarray
     * @param high the ending index of the subarray
     */
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    /**
     * Partitions the array around a randomly chosen pivot using the Lomuto partition scheme.
     *
     * <p>This is a private helper method used by {@link #quickSort(int[], int, int)}.</p>
     *
     * @param arr  the array to partition
     * @param low  starting index
     * @param high ending index
     * @return the final index position of the pivot after partitioning
     */
    private static int partition(int[] arr, int low, int high) {
        // Select random pivot and move it to the end
        int randomIndex = low + (int)(Math.random() * (high - low + 1));
        swap(arr, randomIndex, high);

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            comparisons++;
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    /**
     * Swaps two elements in the array and increments the swap counter.
     *
     * <p>This is a private helper method used by Quick Sort's partition step.</p>
     *
     * @param arr the array
     * @param i   index of first element
     * @param j   index of second element
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        swaps++;
    }
}