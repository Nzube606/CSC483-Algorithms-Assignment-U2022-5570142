package Search_Optimization; // Search_Optimization.Main.java

import java.util.*;

/**
 * Main driver class for TechMart search performance analysis.
 *
 * <p>This class generates product data, measures and compares the performance
 * of sequential search vs binary search on product IDs, and demonstrates the
 * hybrid catalog's name-based search capabilities. Results are formatted for
 * assignment submission.</p>
 *
 * @author Ilodigwe Nzube
 * @version 1.0
 * @since 2024
 */
public class Main {

    /**
     * Generates an array of random products for testing.
     *
     * <p>Each product receives a random ID (1-200,000), a unique name,
     * a category based on index modulo 5, a random price (0-1000), and
     * a random stock quantity (0-99).</p>
     *
     * @param size the number of products to generate
     * @return an array of randomly generated Product objects
     */
    public static Product[] generateProducts(int size) {
        Product[] products = new Product[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            int id = rand.nextInt(200000) + 1; // IDs 1–200,000
            products[i] = new Product(
                    id,
                    "Search_Optimization.Product" + i,
                    "Category" + (i % 5),
                    rand.nextDouble() * 1000,
                    rand.nextInt(100)
            );
        }
        return products;
    }

    /**
     * Sorts an array of products by their product ID in ascending order.
     *
     * @param products the array of products to sort (modified in-place)
     */
    public static void sortById(Product[] products) {
        Arrays.sort(products, Comparator.comparingInt(Product::getProductId));
    }

    /**
     * Measures the execution time of sequential search for a given target ID.
     *
     * @param products the array to search through
     * @param target   the product ID to search for
     * @return the search duration in nanoseconds
     */
    public static long measureSequential(Product[] products, int target) {
        long start = System.nanoTime();
        ProductSearch.sequentialSearchById(products, target);
        long end = System.nanoTime();
        return end - start;
    }

    /**
     * Measures the execution time of binary search for a given target ID.
     *
     * @param products the sorted array to search through
     * @param target   the product ID to search for
     * @return the search duration in nanoseconds
     */
    public static long measureBinary(Product[] products, int target) {
        long start = System.nanoTime();
        ProductSearch.binarySearchById(products, target);
        long end = System.nanoTime();
        return end - start;
    }

    /**
     * Main entry point that runs the complete search performance analysis.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Generates 100,000 random products</li>
     *   <li>Measures sequential and binary search performance for best, average, and worst cases</li>
     *   <li>Calculates the performance improvement factor</li>
     *   <li>Tests the hybrid catalog's name-based search and insertion times</li>
     *   <li>Prints formatted results for assignment submission</li>
     * </ol>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        int size = 100_000;
        System.out.println("Generating products...");
        Product[] products = generateProducts(size);

        // Copy and sort for binary search
        Product[] sortedProducts = Arrays.copyOf(products, products.length);
        sortById(sortedProducts);

        // Test case IDs
        int bestCaseId = products[0].getProductId();        // first element
        int avgCaseId = products[size / 2].getProductId();  // middle element
        int worstCaseId = -1;                               // not found

        // Measure sequential and binary searches
        long seqBest = measureSequential(products, bestCaseId);
        long binBest = measureBinary(sortedProducts, bestCaseId);

        long seqAvg = measureSequential(products, avgCaseId);
        long binAvg = measureBinary(sortedProducts, avgCaseId);

        long seqWorst = measureSequential(products, worstCaseId);
        long binWorst = measureBinary(sortedProducts, worstCaseId);

        long improvement = seqAvg / binAvg;

        // Hybrid catalog setup
        HybridProductCatalog catalog = new HybridProductCatalog(size + 10);
        long hybridInsertTotal = 0;
        long hybridSearchTotal = 0;

        Random rand = new Random();

        // Measure hybrid catalog insertion times for all products
        for (Product p : products) {
            long start = System.nanoTime();
            catalog.addProduct(p);
            long end = System.nanoTime();
            hybridInsertTotal += (end - start);
        }

        // Measure hybrid name searches (1000 random lookups)
        for (int i = 0; i < 1000; i++) {
            int idx = rand.nextInt(size);
            Product toSearch = products[idx];
            long start = System.nanoTime();
            catalog.searchByName(toSearch.getProductName());
            long end = System.nanoTime();
            hybridSearchTotal += (end - start);
        }

        double hybridInsertAvg = hybridInsertTotal / 1_000_000.0 / size;
        double hybridSearchAvg = hybridSearchTotal / 1_000_000.0 / 1000;

        // Output in assignment-style format
        System.out.println("================================================================");
        System.out.println("TECHMART SEARCH PERFORMANCE ANALYSIS (n = " + size + " products)");
        System.out.println("================================================================");

        System.out.println("\nSEQUENTIAL SEARCH:");
        System.out.printf("Best Case (ID found at position 0): %.3f ms%n", seqBest / 1_000_000.0);
        System.out.printf("Average Case (random ID): %.3f ms%n", seqAvg / 1_000_000.0);
        System.out.printf("Worst Case (ID not found): %.3f ms%n", seqWorst / 1_000_000.0);

        System.out.println("\nBINARY SEARCH:");
        System.out.printf("Best Case (ID at middle): %.3f ms%n", binBest / 1_000_000.0);
        System.out.printf("Average Case (random ID): %.3f ms%n", binAvg / 1_000_000.0);
        System.out.printf("Worst Case (ID not found): %.3f ms%n", binWorst / 1_000_000.0);

        System.out.printf("\nPERFORMANCE IMPROVEMENT: Binary search is ~%dx faster on average%n", improvement);

        System.out.println("\nHYBRID NAME SEARCH:");
        System.out.printf("Average search time: %.3f ms%n", hybridSearchAvg);
        System.out.printf("Average insert time: %.3f ms%n", hybridInsertAvg);

        System.out.println("================================================================");
    }
}