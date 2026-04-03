package Search_Optimization;// Search_Optimization.Main.java
import java.util.*;

public class Main {

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

    public static void sortById(Product[] products) {
        Arrays.sort(products, Comparator.comparingInt(Product::getProductId));
    }

    public static long measureSequential(Product[] products, int target) {
        long start = System.nanoTime();
        ProductSearch.sequentialSearchById(products, target);
        long end = System.nanoTime();
        return end - start;
    }

    public static long measureBinary(Product[] products, int target) {
        long start = System.nanoTime();
        ProductSearch.binarySearchById(products, target);
        long end = System.nanoTime();
        return end - start;
    }

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