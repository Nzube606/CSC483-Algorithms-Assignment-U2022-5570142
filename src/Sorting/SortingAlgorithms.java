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
        while (i <= mid)  { temp[k++] = arr[i++]; swaps++; }

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
    // Quick Sort
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        // --- FIX: Randomly pick a pivot and swap it to the end ---
        // Without this, sorted/reverse-sorted input causes O(n) recursion
        // depth (every partition picks the smallest/largest element as pivot),
        // which overflows the call stack at large n (e.g. 100,000)
        int randomIndex = low + (int)(Math.random() * (high - low + 1));
        swap(arr, randomIndex, high);   // Move random pivot to end (Lomuto scheme)

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
    // =========================================================================
    // HEAP SORT  —  O(n log n) always (best, average, worst)
    // Strategy: build a max-heap from the array, then repeatedly extract
    //           the maximum element (root) and place it at the end.
    // In-place. Not stable. Uses O(1) extra memory.
    // =========================================================================
    public static void heapSort(int[] arr) {
        int n = arr.length;

        // --- Phase 1: Build a max-heap ---
        // Start from the last non-leaf node (n/2 - 1) and heapify downward.
        // After this loop, arr[0] holds the largest element.
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // --- Phase 2: Extract elements from heap one by one ---
        // Swap root (current max) with the last unsorted element,
        // reduce heap size by 1, then restore heap property.
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);    // Move current maximum to its sorted position
            heapify(arr, i, 0); // Re-heapify the reduced heap (size = i)
        }
    }

    /**
     * Ensures the subtree rooted at index i satisfies the max-heap property.
     * Assumes both child subtrees are already valid max-heaps.
     *
     * @param arr  the array representing the heap
     * @param n    the current heap size (elements beyond n are already sorted)
     * @param i    the root index of the subtree to heapify
     */
    private static void heapify(int[] arr, int n, int i) {
        int largest = i;        // Assume the root is the largest
        int left  = 2 * i + 1; // Left child index in zero-based array
        int right = 2 * i + 2; // Right child index in zero-based array

        // Check if left child exists and is larger than current largest
        if (left < n) {
            comparisons++;
            if (arr[left] > arr[largest]) largest = left;
        }

        // Check if right child exists and is larger than current largest
        if (right < n) {
            comparisons++;
            if (arr[right] > arr[largest]) largest = right;
        }

        // If the largest element is not the root, swap and continue heapifying
        // down the affected subtree (recursive until heap property is restored)
        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }

    // -------------------------------------------------------------------------
    // Shared swap helper — swaps arr[i] and arr[j] and increments swap counter.
    // Used by Quick Sort, Heap Sort, and their helper methods.
    // -------------------------------------------------------------------------
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        swaps++;    // Count every in-place swap
    }
}