package Sorting;

import java.util.Random;

public class DataGenerator {

    private static Random rand = new Random();

    // RANDOM DATA
    // Generates completely random values
    public static int[] generateRandom(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(100000); // random values up to 100,000
        }

        return arr;
    }


    // SORTED DATA
    // Generates already sorted array (ascending)
    public static int[] generateSorted(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = i; // 0,1,2,3,...
        }

        return arr;
    }


    // REVERSE SORTED
    // Generates descending order (worst case for some algorithms)
    public static int[] generateReverseSorted(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = size - i; // e.g., 100,99,98,...
        }

        return arr;
    }


    // NEARLY SORTED
    // 90% sorted, 10% randomly swapped
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


    // MANY DUPLICATES
    // Only 10 distinct values repeated
    public static int[] generateDuplicates(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10); // values from 0–9
        }

        return arr;
    }
}