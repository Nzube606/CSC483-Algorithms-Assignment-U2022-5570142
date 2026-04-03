package Sorting;

public class SortingAlgorithms {

    // -------------------------------------------------------------------------
    // Shared counters — static so all sorting methods update the same variables.
    // resetCounters() must be called before each timed run to get clean counts.
    // -------------------------------------------------------------------------
    private static long comparisons = 0;
    private static long swaps = 0;

    // --- Getters (used by SortingExperiment after each run) ---
    public static long getComparisons() { return comparisons; }
    public static long getSwaps()       { return swaps; }

    /** Resets both counters to zero before each algorithm run */
    public static void resetCounters() {
        comparisons = 0;
        swaps = 0;
    }

    // =========================================================================
    // INSERTION SORT  —  O(n²) average/worst,  O(n) best (already sorted)
    // Strategy: grow a sorted region left-to-right; shift elements right
    //           until the correct position for the current key is found.
    // Best for: small arrays or nearly-sorted data.
    // =========================================================================
    public static void insertionSort(int[] arr) {
        resetCounters();
        int n = arr.length;

        // Start from index 1; index 0 is trivially "sorted"
        for (int i = 1; i < n; i++) {
            int key = arr[i];   // Element to be inserted into sorted region
            int j = i - 1;

            // Shift elements of the sorted region that are greater than key
            // one position to the right, making room for key
            while (j >= 0) {
                comparisons++;          // Count every element-to-key comparison
                if (arr[j] > key) {
                    swaps++;            // Count each rightward shift as a swap
                    arr[j + 1] = arr[j];
                    j--;
                } else {
                    break;              // Correct position found; stop shifting
                }
            }

            // Place key into its correct sorted position
            arr[j + 1] = key;
            swaps++;    // Count the final placement as a swap/assignment
        }
    }

    // =========================================================================
    // MERGE SORT  —  O(n log n) always (best, average, worst)
    // Strategy: divide array in half recursively, then merge sorted halves.
    // Stable sort. Uses O(n) extra memory for temporary arrays.
    // Swaps are marked N/A in output — this sort copies, it doesn't swap in-place.
    // =========================================================================
    public static void mergeSort(int[] arr, int left, int right) {
        // Base case: a single element is already sorted
        if (left < right) {
            int mid = left + (right - left) / 2;    // Midpoint (avoids int overflow)

            mergeSort(arr, left, mid);               // Sort left half
            mergeSort(arr, mid + 1, right);          // Sort right half
            merge(arr, left, mid, right);            // Merge the two sorted halves
        }
    }

    /**
     * Merges two adjacent sorted sub-arrays:
     *   Left  sub-array: arr[left..mid]
     *   Right sub-array: arr[mid+1..right]
     * Uses a temporary array to hold merged result, then copies back.
     */
    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];  // Temporary storage for merged result
        int i = left;       // Pointer into left sub-array
        int j = mid + 1;    // Pointer into right sub-array
        int k = 0;          // Pointer into temp array

        // Compare elements from both halves and pick the smaller one
        while (i <= mid && j <= right) {
            comparisons++;                  // Count each head-to-head comparison
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];       // Left element wins (or tie — stable)
            } else {
                temp[k++] = arr[j++];       // Right element wins
            }
            swaps++;    // Each element placed into temp counts as an assignment
        }

        // Copy any remaining elements from the left half
        while (i <= mid)   { temp[k++] = arr[i++]; swaps++; }

        // Copy any remaining elements from the right half
        while (j <= right) { temp[k++] = arr[j++]; swaps++; }

        // Write merged result back into original array
        for (i = left, k = 0; i <= right; i++, k++) {
            arr[i] = temp[k];
            swaps++;    // Count each write-back as an assignment
        }
    }

    // =========================================================================
    // QUICK SORT  —  O(n log n) average,  O(n²) worst (sorted input + bad pivot)
    // Strategy: pick a pivot, partition array so everything < pivot is left,
    //           everything > pivot is right, then recursively sort both sides.
    // In-place. Not stable. Fast in practice due to cache efficiency.
    // =========================================================================
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);   // Recursively sort left of pivot
            quickSort(arr, pi + 1, high);  // Recursively sort right of pivot
        }
    }

    /**
     * Lomuto partition scheme:
     * - Uses a randomly chosen pivot to avoid O(n²) on sorted/reverse input.
     * - Scans left-to-right; swaps elements <= pivot to the left partition.
     * - Places pivot at its final sorted position and returns that index.
     */
    private static int partition(int[] arr, int low, int high) {
        // Randomly pick a pivot and swap it to the end
        // Without this, sorted/reverse-sorted input causes O(n) recursion
        // depth, which overflows the call stack at large n (e.g. 100,000)
        int randomIndex = low + (int)(Math.random() * (high - low + 1));
        swap(arr, randomIndex, high);   // Move random pivot to end (Lomuto scheme)

        int pivot = arr[high];
        int i = low - 1;    // Tracks boundary of the "less-than" region

        for (int j = low; j < high; j++) {
            comparisons++;              // Compare current element to pivot
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);        // Move element into left partition
            }
        }

        // Place pivot between left (<= pivot) and right (> pivot) partitions
        swap(arr, i + 1, high);
        return i + 1;   // Return pivot's final sorted index
    }

    // -------------------------------------------------------------------------
    // Shared swap helper — swaps arr[i] and arr[j] and increments swap counter.
    // Used by Quick Sort and its helper methods.
    // -------------------------------------------------------------------------
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        swaps++;    // Count every in-place swap
    }
}