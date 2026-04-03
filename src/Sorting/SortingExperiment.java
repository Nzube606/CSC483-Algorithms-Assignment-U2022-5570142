package Sorting;

import java.util.*;

public class SortingExperiment {

    private static final int RUNS = 5;

    // =====================================================================
    // CORE MEASUREMENT - Returns mean time, std dev, comparisons & swaps
    // =====================================================================
    public static ExperimentResultWithTimes runAndMeasure(int[] original, String algorithm) {
        List<Double> runTimes = new ArrayList<>();
        long finalComparisons = 0;
        long finalSwaps = 0;

        for (int r = 0; r < RUNS; r++) {
            int[] copy = Arrays.copyOf(original, original.length);
            SortingAlgorithms.resetCounters();

            long start = System.nanoTime();

            switch (algorithm) {
                case "Insertion" -> SortingAlgorithms.insertionSort(copy);
                case "Merge"     -> SortingAlgorithms.mergeSort(copy, 0, copy.length - 1);
                case "Quick"     -> SortingAlgorithms.quickSort(copy, 0, copy.length - 1);
            }

            long end = System.nanoTime();
            double timeMs = (end - start) / 1_000_000.0;

            runTimes.add(timeMs);
            finalComparisons = SortingAlgorithms.getComparisons();
            finalSwaps = SortingAlgorithms.getSwaps();
        }

        double meanTime = runTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double stdDev = calculateStdDev(runTimes);

        return new ExperimentResultWithTimes(meanTime, stdDev, finalComparisons, finalSwaps, runTimes);
    }

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
    // SELF-CONTAINED DATASET GENERATOR (Fixed the missing method)
    // =====================================================================
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
                // Disrupt 10% of the elements
                int disruptions = (int) (size * 0.10);
                for (int k = 0; k < disruptions; k++) {
                    int idx = rand.nextInt(size);
                    arr[idx] = rand.nextInt(1_000_000);
                }
                break;

            case "duplicates":
                // Only 10 distinct values
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
    // TABLE PRINTING WITH STATISTICS
    // =====================================================================
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

            // Insertion Sort
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

    private static void printStatRow(int size, String algorithm, ExperimentResultWithTimes result) {
        String swapsStr = algorithm.equals("Merge") ? "N/A" : String.format("%,d", result.swaps);

        System.out.printf("| %-14d | %-12s | %11.3f | %13.3f | %,14d | %-20s |%n",
                size, algorithm, result.meanTime, result.stdDev, result.comparisons, swapsStr);
    }

    // =====================================================================
    // MAIN
    // =====================================================================
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
        System.out.println("- Mean = Average runtime across 5 runs");
        System.out.println("- Std Dev = Measure of variability (lower = more consistent)");
        System.out.println("- Merge Sort usually has lower Std Dev (more predictable)");
        System.out.println("=".repeat(110));

        System.out.println("\nCONCLUSIONS:");
        System.out.println("- Quick Sort is fastest on random and nearly-sorted data");
        System.out.println("- Insertion Sort performs well only for small n and nearly-sorted inputs");
        System.out.println("- Merge Sort is the most consistent across all data types");
        System.out.println("- Insertion Sort becomes impractical for large random/reverse data");
    }
}

// =============================================================================
// Result Holder Class
// =============================================================================
class ExperimentResultWithTimes {
    double meanTime;
    double stdDev;
    long comparisons;
    long swaps;
    List<Double> individualTimes;

    ExperimentResultWithTimes(double meanTime, double stdDev, long comparisons, long swaps, List<Double> times) {
        this.meanTime = meanTime;
        this.stdDev = stdDev;
        this.comparisons = comparisons;
        this.swaps = swaps;
        this.individualTimes = times;
    }
}