package Sorting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SortingAlgorithms class.
 *
 * <p>This class verifies correctness of all sorting algorithms
 * using different dataset types and edge cases.</p>
 */
public class SortingAlgorithmsTest {

    /**
     * Helper method to check if array is sorted in ascending order.
     */
    private boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                return false;
            }
        }
        return true;
    }

    // =========================
    // INSERTION SORT TESTS
    // =========================

    @Test
    void testInsertionSortRandom() {
        int[] arr = DataGenerator.generateRandom(1000);
        SortingAlgorithms.insertionSort(arr);
        assertTrue(isSorted(arr));
    }

    @Test
    void testInsertionSortSorted() {
        int[] arr = DataGenerator.generateSorted(1000);
        SortingAlgorithms.insertionSort(arr);
        assertTrue(isSorted(arr));
    }

    @Test
    void testInsertionSortReverse() {
        int[] arr = DataGenerator.generateReverseSorted(1000);
        SortingAlgorithms.insertionSort(arr);
        assertTrue(isSorted(arr));
    }

    // =========================
    // MERGE SORT TESTS
    // =========================

    @Test
    void testMergeSortRandom() {
        int[] arr = DataGenerator.generateRandom(1000);
        SortingAlgorithms.mergeSort(arr, 0, arr.length - 1);
        assertTrue(isSorted(arr));
    }

    @Test
    void testMergeSortNearlySorted() {
        int[] arr = DataGenerator.generateNearlySorted(1000);
        SortingAlgorithms.mergeSort(arr, 0, arr.length - 1);
        assertTrue(isSorted(arr));
    }

    @Test
    void testMergeSortDuplicates() {
        int[] arr = DataGenerator.generateDuplicates(1000);
        SortingAlgorithms.mergeSort(arr, 0, arr.length - 1);
        assertTrue(isSorted(arr));
    }

    // =========================
    // QUICK SORT TESTS
    // =========================

    @Test
    void testQuickSortRandom() {
        int[] arr = DataGenerator.generateRandom(1000);
        SortingAlgorithms.quickSort(arr, 0, arr.length - 1);
        assertTrue(isSorted(arr));
    }

    @Test
    void testQuickSortReverse() {
        int[] arr = DataGenerator.generateReverseSorted(1000);
        SortingAlgorithms.quickSort(arr, 0, arr.length - 1);
        assertTrue(isSorted(arr));
    }

    @Test
    void testQuickSortDuplicates() {
        int[] arr = DataGenerator.generateDuplicates(1000);
        SortingAlgorithms.quickSort(arr, 0, arr.length - 1);
        assertTrue(isSorted(arr));
    }

    // =========================
    // EDGE CASE TESTS
    // =========================

    @Test
    void testEmptyArray() {
        int[] arr = {};
        SortingAlgorithms.insertionSort(arr);
        assertEquals(0, arr.length);
    }

    @Test
    void testSingleElement() {
        int[] arr = {5};
        SortingAlgorithms.quickSort(arr, 0, arr.length - 1);
        assertTrue(isSorted(arr));
    }
}