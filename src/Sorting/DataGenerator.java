package Sorting;

import java.util.Random;

/**
 * Generates test datasets of various types for sorting algorithm empirical analysis.
 *
 * <p>This class provides methods to create arrays with different characteristics
 * including random order, sorted order, reverse sorted order, nearly sorted order,
 * and arrays with many duplicate values. These datasets are used to test sorting
 * algorithm performance under different conditions.</p>
 *
 * @author Ilodigwe Nzube
 * @version 1.0
 * @since 2024
 */
public class DataGenerator {

    /** Random number generator used for all random value generation */
    private static Random rand = new Random();

    /**
     * Generates an array with completely random values.
     *
     * <p>Each element is independently assigned a random integer between 0 and 99,999.
     * This represents the typical "average case" for most sorting algorithms.</p>
     *
     * @param size the number of elements to generate
     * @return a new array filled with random values
     */
    public static int[] generateRandom(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(100000); // random values up to 100,000
        }

        return arr;
    }

    /**
     * Generates an already sorted array in ascending order.
     *
     * <p>The array contains values 0, 1, 2, ..., size-1. This represents the
     * "best case" for algorithms like Insertion Sort and Bubble Sort, but can be
     * problematic for naive Quick Sort implementations.</p>
     *
     * @param size the number of elements to generate
     * @return a new array sorted in ascending order
     */
    public static int[] generateSorted(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = i; // 0,1,2,3,...
        }

        return arr;
    }

    /**
     * Generates a reverse sorted array in descending order.
     *
     * <p>The array contains values size, size-1, size-2, ..., 1. This represents the
     * "worst case" for algorithms like Insertion Sort and Bubble Sort, and can also
     * cause poor performance in Quick Sort without proper pivot selection.</p>
     *
     * @param size the number of elements to generate
     * @return a new array sorted in descending order
     */
    public static int[] generateReverseSorted(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = size - i; // e.g., 100,99,98,...
        }

        return arr;
    }

    /**
     * Generates a nearly sorted array where 90% of elements are in order.
     *
     * <p>The method starts with a fully sorted array, then randomly swaps
     * approximately 10% of the elements. This simulates real-world scenarios
     * where data is mostly ordered with minor disturbances.</p>
     *
     * @param size the number of elements to generate
     * @return a new array that is approximately 90% sorted
     */
    public static int[] generateNearlySorted(int size) {
        int[] arr = generateSorted(size);

        int swaps = size / 10; // 10% of elements

        for (int i = 0; i < swaps; i++) {
            int idx1 = rand.nextInt(size);
            int idx2 = rand.nextInt(size);

            // swap two elements to introduce slight disorder
            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }

        return arr;
    }

    /**
     * Generates an array with many duplicate values (only 10 distinct values).
     *
     * <p>Each element is randomly assigned a value between 0 and 9. This tests
     * algorithm performance on datasets with low cardinality, where stability
     * and duplicate handling become important factors.</p>
     *
     * @param size the number of elements to generate
     * @return a new array containing only values from 0 to 9 repeated many times
     */
    public static int[] generateDuplicates(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10); // values from 0–9
        }

        return arr;
    }
}