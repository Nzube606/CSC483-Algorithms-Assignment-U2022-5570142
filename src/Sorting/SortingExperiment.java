package Sorting;

import java.util.Arrays;

public class SortingExperiment {

    // Number of timed runs per algorithm/dataset combination.
    // The average across all runs smooths out JVM warm-up and OS scheduling noise.
    private static final int RUNS = 5;

    // =========================================================================
    // CORE MEASUREMENT ENGINE
    // Accepts RUNS pre-built Runnable (each wrapping a fresh array copy),
    // executes them in sequence, accumulates total time, and returns an
    // ExperimentResult with the averaged time + final run's comparison/swap counts.
    // =========================================================================

    /**
     * Runs the algorithm RUNS times and returns averaged timing results.
     *
     * @param original         the original unsorted array (unused here, kept for context)
     * @param algorithmSupplier array of RUNS Runnables, each containing a fresh array copy
     * @return ExperimentResult with avg time (ms), comparisons, and swaps
     */
    public static ExperimentResult runAndMeasure(int[] original, Runnable[] algorithmSupplier) {
        double totalTime = 0;
        ExperimentResult lastResult = null;

        for (int r = 0; r < RUNS; r++) {
            SortingAlgorithms.resetCounters();  // Clear counts before each run

            long start = System.nanoTime();
            algorithmSupplier[r].run();         // Execute this run's sort
            long end   = System.nanoTime();

            double runTimeMs = (end - start) / 1_000_000.0;
            totalTime += runTimeMs;

            // Save result from this run; comparisons/swaps are deterministic
            // (same input → same count every run), so the last run's values are reliable
            lastResult = new ExperimentResult(
                    runTimeMs,
                    SortingAlgorithms.getComparisons(),
                    SortingAlgorithms.getSwaps()
            );
        }

        double avgTime = totalTime / RUNS;  // Average execution time over all runs
        return new ExperimentResult(avgTime, lastResult.comparisons, lastResult.swaps);
    }

    // =========================================================================
    // PER-ALGORITHM MEASUREMENT WRAPPERS
    // Each method pre-creates RUNS independent array copies so that run 2–5
    // don't accidentally sort an already-sorted array (which would skew results).
    // =========================================================================

    /** Measures Insertion Sort: RUNS fresh copies, averaged time */
    public static ExperimentResult measureInsertion(int[] original) {
        Runnable[] runs = new Runnable[RUNS];
        for (int r = 0; r < RUNS; r++) {
            int[] copy = Arrays.copyOf(original, original.length); // Fresh unsorted copy
            runs[r] = () -> SortingAlgorithms.insertionSort(copy);
        }
        return runAndMeasure(original, runs);
    }

    /** Measures Merge Sort: RUNS fresh copies, averaged time */
    public static ExperimentResult measureMerge(int[] original) {
        Runnable[] runs = new Runnable[RUNS];
        for (int r = 0; r < RUNS; r++) {
            int[] copy = Arrays.copyOf(original, original.length);
            runs[r] = () -> SortingAlgorithms.mergeSort(copy, 0, copy.length - 1);
        }
        return runAndMeasure(original, runs);
    }

    /** Measures Quick Sort: RUNS fresh copies, averaged time */
    public static ExperimentResult measureQuick(int[] original) {
        Runnable[] runs = new Runnable[RUNS];
        for (int r = 0; r < RUNS; r++) {
            int[] copy = Arrays.copyOf(original, original.length);
            runs[r] = () -> SortingAlgorithms.quickSort(copy, 0, copy.length - 1);
        }
        return runAndMeasure(original, runs);
    }

    /** Measures Heap Sort: RUNS fresh copies, averaged time */
    public static ExperimentResult measureHeap(int[] original) {
        Runnable[] runs = new Runnable[RUNS];
        for (int r = 0; r < RUNS; r++) {
            int[] copy = Arrays.copyOf(original, original.length);
            runs[r] = () -> SortingAlgorithms.heapSort(copy);
        }
        return runAndMeasure(original, runs);
    }

    // =========================================================================
    // TABLE FORMATTING HELPERS
    // =========================================================================

    // Reusable horizontal borderline for the output table
    static String border = "+" + "-".repeat(16) + "+" + "-".repeat(14) + "+" +
            "-".repeat(14) + "+" + "-".repeat(17) + "+" + "-".repeat(16) + "+";

    /**
     * Prints a single formatted data row in the results table.
     * Swaps column accepts a String to allow "N/A" for Merge Sort.
     */
    public static void printRow(int size, String algorithm,
                                double timeMs, long comparisons, String swaps) {
        System.out.printf("| %-14d | %-12s | %-12.4f | %-15s | %-14s |%n",
                size, algorithm, timeMs,
                String.format("%,d", comparisons),  // e.g. 1,234,567
                swaps);
    }

    /**
     * Prints a full results table for one dataset type (e.g. Random, Sorted).
     * Iterates over all input sizes and runs all four algorithms for each.
     * Insertion Sort is flagged as TOO SLOW for n = 100,000 to avoid long waits.
     */
    public static void printTableForDataset(String datasetName, int[] sizes) {
        // Section header for this dataset
        System.out.println();
        System.out.println("=".repeat(83));
        System.out.printf("  SORTING ALGORITHMS COMPARISON - %s%n", datasetName.toUpperCase());
        System.out.println("=".repeat(83));

        // Column headers
        System.out.println(border);
        System.out.printf("| %-14s | %-12s | %-12s | %-15s | %-14s |%n",
                "Input Size", "Algorithm", "Time (ms)", "Comparisons", "Swaps");
        System.out.println(border);

        for (int size : sizes) {
            // Generate the dataset for this size and type
            int[] data = generateDataset(datasetName, size);

            // --- Insertion Sort ---
            // O(n²) makes it impractically slow at n=100,000; flag it rather than freeze
            if (size <= 10000) {
                ExperimentResult ins = measureInsertion(data);
                printRow(size, "Insertion", ins.time, ins.comparisons,
                        String.format("%,d", ins.swaps));
            } else {
                System.out.printf("| %-14d | %-12s | %-12s | %-15s | %-14s |%n",
                        size, "Insertion", "TOO SLOW", "-", "-");
            }

            // --- Merge Sort ---
            // Swaps shown as N/A: merge sort copies data into temp arrays
            // rather than performing in-place swaps, so swap count is not meaningful
            ExperimentResult merge = measureMerge(data);
            printRow(size, "Merge", merge.time, merge.comparisons, "N/A");

            // --- Quick Sort ---
            ExperimentResult quick = measureQuick(data);
            printRow(size, "Quick", quick.time, quick.comparisons,
                    String.format("%,d", quick.swaps));

            // --- Heap Sort ---
            ExperimentResult heap = measureHeap(data);
            printRow(size, "Heap", heap.time, heap.comparisons,
                    String.format("%,d", heap.swaps));

            System.out.println(border); // Separator after each size block
        }
    }

    /**
     * Routes dataset generation to the correct DataGenerator method by name.
     * Keeps printTableForDataset() clean by centralising the switch logic here.
     */
    public static int[] generateDataset(String type, int size) {
        switch (type) {
            case "Random":        return DataGenerator.generateRandom(size);
            case "Sorted":        return DataGenerator.generateSorted(size);
            case "Reverse":       return DataGenerator.generateReverseSorted(size);
            case "Nearly Sorted": return DataGenerator.generateNearlySorted(size);
            case "Duplicates":    return DataGenerator.generateDuplicates(size);
            default:              return DataGenerator.generateRandom(size);
        }
    }

    // =========================================================================
    // MAIN — Entry point: runs the full experiment across all datasets and sizes
    // =========================================================================
    public static void main(String[] args) {

        int[]    sizes    = {100, 1000, 10000, 100000};
        String[] datasets = {"Random", "Sorted", "Reverse", "Nearly Sorted", "Duplicates"};

        // Overall experiment banner
        System.out.println("=".repeat(83));
        System.out.println("       SORTING PERFORMANCE ANALYSIS — EMPIRICAL STUDY");
        System.out.println("       Algorithms: Insertion, Merge, Quick, Heap");
        System.out.println("       Each time value = average of " + RUNS + " runs");
        System.out.println("=".repeat(83));

        // Print one full table per dataset type
        for (String dataset : datasets) {
            printTableForDataset(dataset, sizes);
        }

        // Final conclusions summarising expected algorithmic behaviour
        System.out.println();
        System.out.println("=".repeat(83));
        System.out.println("CONCLUSIONS:");
        System.out.println("- Quick Sort is fastest on average for random data");
        System.out.println("- Insertion Sort is competitive only for n <= 1,000");
        System.out.println("- Merge Sort provides consistent performance regardless of data order");
        System.out.println("- Heap Sort uses O(1) extra space but is slower than Quick Sort in practice");
        System.out.println("- Insertion Sort excels on nearly-sorted data due to early loop termination");
        System.out.println("- Quick Sort may degrade on sorted/reverse data without pivot randomization");
        System.out.println("=".repeat(83));
    }
}

// =============================================================================
// ExperimentResult — simple data carrier for one algorithm run's metrics.
// Holds averaged time (ms) and comparison/swap counts from the final run.
// =============================================================================
class ExperimentResult {
    double time;        // Average execution time in milliseconds across runs
    long comparisons;   // Number of element comparisons made during sorting
    long swaps;         // Number of swaps or assignments made during sorting

    ExperimentResult(double time, long comparisons, long swaps) {
        this.time = time;
        this.comparisons = comparisons;
        this.swaps = swaps;
    }
}