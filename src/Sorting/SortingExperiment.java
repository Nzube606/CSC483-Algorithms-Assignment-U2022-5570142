package Sorting;

import java.util.*;

/**
 * Main driver class for the empirical analysis of sorting algorithms.
 *
 * <p>This class performs a comprehensive experimental study by:
 * <ul>
 *   <li>Generating five different types of test datasets as specified in the assignment</li>
 *   <li>Executing Insertion Sort, Merge Sort, and Quick Sort on each dataset</li>
 *   <li>Measuring execution time (averaged over multiple runs)</li>
 *   <li>Collecting comparison and swap/assignment counts</li>
 *   <li>Computing statistical metrics: mean runtime and standard deviation</li>
 *   <li>Printing formatted comparison tables for analysis</li>
 * </ul>
 *
 * @author Ilodigwe Nzube
 * @version 1.0
 */
public class SortingExperiment {

    /** Number of independent runs per algorithm on each dataset for statistical reliability */
    private static final int RUNS = 5;

    // =====================================================================
    // CORE MEASUREMENT METHODS
    // =====================================================================

    /**
     * Executes the specified sorting algorithm multiple times on a dataset
     * and collects performance statistics including mean time and standard deviation.
     *
     * @param original  the original dataset to be sorted (fresh copies are made for each run)
     * @param algorithm the name of the algorithm to execute ("Insertion", "Merge", or "Quick")
     * @return an {@link ExperimentResultWithTimes} containing mean time, std dev,
     *         comparisons, swaps, and individual run times
     */
    public static ExperimentResultWithTimes runAndMeasure(int[] original, String algorithm) {
        List<Double> runTimes = new ArrayList<>();
        long finalComparisons = 0;
        long finalSwaps = 0;

        for (int r = 0; r < RUNS; r++) {
            // Create a fresh copy for each run to ensure independent measurements
            int[] copy = Arrays.copyOf(original, original.length);
            SortingAlgorithms.resetCounters();

            long start = System.nanoTime();

            switch (algorithm) {
                case "Insertion" -> SortingAlgorithms.insertionSort(copy);
                case "Merge"     -> SortingAlgorithms.mergeSort(copy, 0, copy.length - 1);
                case "Quick"     -> SortingAlgorithms.quickSort(copy, 0, copy.length - 1);
                default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
            }

            long end = System.nanoTime();
            double timeMs = (end - start) / 1_000_000.0; // Convert to milliseconds

            runTimes.add(timeMs);
            finalComparisons = SortingAlgorithms.getComparisons();
            finalSwaps = SortingAlgorithms.getSwaps();
        }

        double meanTime = runTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double stdDev = calculateStdDev(runTimes);

        return new ExperimentResultWithTimes(meanTime, stdDev, finalComparisons, finalSwaps, runTimes);
    }

    /**
     * Calculates the sample standard deviation of a list of runtime values.
     *
     * @param values list of execution times in milliseconds
     * @return the standard deviation, or 0.0 if fewer than 2 values are provided
     */
    private static double calculateStdDev(List<Double> values) {
        if (values.size() < 2) return 0.0;

        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average()
                .orElse(0.0);

        return Math.sqrt(variance);
    }

    // =====================================================================
    // DATASET GENERATION
    // =====================================================================

    /**
     * Generates test datasets with specific characteristics as required by the assignment.
     *
     * <p>Supported dataset types:</p>
     * <ul>
     *   <li>"Random"        - completely random order</li>
     *   <li>"Sorted"        - already sorted in ascending order</li>
     *   <li>"Reverse"       - sorted in descending order</li>
     *   <li>"Nearly Sorted" - 90% sorted with 10% random disruptions</li>
     *   <li>"Duplicates"    - only 10 distinct values repeated many times</li>
     * </ul>
     *
     * @param type the type of dataset to generate
     * @param size the number of elements in the dataset
     * @return a new integer array containing the generated test data
     */
    public static int[] generateDataset(String type, int size) {
        int[] arr = new int[size];
        Random rand = new Random(42);   // Fixed seed for reproducible results

        switch (type.toLowerCase()) {
            case "random":
                for (int i = 0; i < size; i++) {
                    arr[i] = rand.nextInt(1_000_000);
                }
                break;

            case "sorted":
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }
                break;

            case "reverse":
                for (int i = 0; i < size; i++) {
                    arr[i] = size - 1 - i;
                }
                break;

            case "nearly sorted":
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }
                // Introduce 10% random disruptions
                int disruptions = (int) (size * 0.10);
                for (int k = 0; k < disruptions; k++) {
                    int idx = rand.nextInt(size);
                    arr[idx] = rand.nextInt(1_000_000);
                }
                break;

            case "duplicates":
                for (int i = 0; i < size; i++) {
                    arr[i] = rand.nextInt(10);
                }
                break;

            default:
                for (int i = 0; i < size; i++) {
                    arr[i] = rand.nextInt(1_000_000);
                }
        }
        return arr;
    }

    // =====================================================================
    // TABLE PRINTING
    // =====================================================================

    /**
     * Prints a formatted results table for one dataset type across all specified input sizes.
     *
     * @param datasetName the name of the dataset type (e.g., "Random", "Sorted")
     * @param sizes       array of input sizes to test
     */
    public static void printTableForDataset(String datasetName, int[] sizes) {
        System.out.println();
        System.out.println("=".repeat(110));
        System.out.printf(" SORTING ALGORITHMS COMPARISON - %s%n", datasetName.toUpperCase());
        System.out.println("=".repeat(110));

        System.out.println("+----------------+--------------+-------------+---------------+----------------+----------------------+");
        System.out.printf("| %-14s | %-12s | %-11s | %-13s | %-14s | %-20s |%n",
                "Input Size", "Algorithm", "Mean (ms)", "Std Dev (ms)", "Comparisons", "Swaps");
        System.out.println("+----------------+--------------+-------------+---------------+----------------+----------------------+");

        for (int size : sizes) {
            int[] data = generateDataset(datasetName, size);

            // Insertion Sort - skipped for large inputs due to quadratic time
            if (size <= 10000) {
                ExperimentResultWithTimes ins = runAndMeasure(data, "Insertion");
                printStatRow(size, "Insertion", ins);
            } else {
                System.out.printf("| %-14d | %-12s | %-11s | %-13s | %-14s | %-20s |%n",
                        size, "Insertion", "TOO SLOW", "-", "-", "-");
            }

            // Merge Sort
            ExperimentResultWithTimes mergeRes = runAndMeasure(data, "Merge");
            printStatRow(size, "Merge", mergeRes);

            // Quick Sort
            ExperimentResultWithTimes quickRes = runAndMeasure(data, "Quick");
            printStatRow(size, "Quick", quickRes);

            System.out.println("+----------------+--------------+-------------+---------------+----------------+----------------------+");
        }
    }

    /**
     * Prints a single statistics row in the results table.
     *
     * @param size      the input size
     * @param algorithm the algorithm name
     * @param result    the statistical results for that algorithm
     */
    private static void printStatRow(int size, String algorithm, ExperimentResultWithTimes result) {
        String swapsStr = algorithm.equals("Merge") ? "N/A" : String.format("%,d", result.swaps);

        System.out.printf("| %-14d | %-12s | %11.3f | %13.3f | %,14d | %-20s |%n",
                size, algorithm, result.meanTime, result.stdDev, result.comparisons, swapsStr);
    }

    // =====================================================================
    // ENTRY POINT
    // =====================================================================

    /**
     * The main entry point of the program.
     *
     * <p>Runs the full empirical experiment across all dataset types and input sizes,
     * then prints formatted tables with statistical analysis.</p>
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        int[] sizes = {100, 1000, 10000, 100000};
        String[] datasets = {"Random", "Sorted", "Reverse", "Nearly Sorted", "Duplicates"};

        System.out.println("=".repeat(110));
        System.out.println(" SORTING PERFORMANCE ANALYSIS — EMPIRICAL + STATISTICAL STUDY");
        System.out.println(" Algorithms: Insertion Sort, Merge Sort, Quick Sort");
        System.out.println(" Time values = average of " + RUNS + " runs | Std Dev shows consistency");
        System.out.println("=".repeat(110));

        for (String dataset : datasets) {
            printTableForDataset(dataset, sizes);
        }

        System.out.println("\n" + "=".repeat(110));
        System.out.println("STATISTICAL SUMMARY:");
        System.out.println("- Mean     = Average runtime across 5 runs");
        System.out.println("- Std Dev  = Measure of variability (lower = more consistent performance)");
        System.out.println("- Merge Sort usually shows lower Std Dev across different data types");
        System.out.println("=".repeat(110));

        System.out.println("\nCONCLUSIONS:");
        System.out.println("- Quick Sort is fastest on random and nearly-sorted data");
        System.out.println("- Insertion Sort performs well only for small n and nearly-sorted inputs");
        System.out.println("- Merge Sort is the most consistent across all data types");
        System.out.println("- Insertion Sort becomes impractical for large random or reverse-sorted data");
    }
}

/**
 * Simple immutable data holder class that stores the statistical results
 * of running one sorting algorithm on a dataset.
 */
class ExperimentResultWithTimes {

    /** Average execution time in milliseconds across all runs */
    double meanTime;

    /** Standard deviation of the execution times */
    double stdDev;

    /** Total number of element comparisons performed */
    long comparisons;

    /** Total number of swaps or assignments performed */
    long swaps;

    /** List of individual run times (for potential future statistical tests) */
    List<Double> individualTimes;

    /**
     * Constructs a new result object containing performance statistics.
     *
     * @param meanTime       average runtime in milliseconds
     * @param stdDev         standard deviation of the runtimes
     * @param comparisons    number of comparisons
     * @param swaps          number of swaps/assignments
     * @param times          list of individual run times
     */
    ExperimentResultWithTimes(double meanTime, double stdDev, long comparisons,
                              long swaps, List<Double> times) {
        this.meanTime = meanTime;
        this.stdDev = stdDev;
        this.comparisons = comparisons;
        this.swaps = swaps;
        this.individualTimes = times;
    }
}