import java.util.Arrays;
import java.util.Random;

public class Main {

    public static Product[] generateProducts(int size) {
        Product[] products = new Product[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            int id = rand.nextInt(200000) + 1;
            products[i] = new Product(
                    id,
                    "Product" + i,
                    "Category" + (i % 5),
                    rand.nextDouble() * 1000,
                    rand.nextInt(100)
            );
        }
        return products;
    }

    public static void sortById(Product[] products) {
        Arrays.sort(products, (a, b) -> Integer.compare(a.getProductId(), b.getProductId()));
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
        int size = 100000;

        System.out.println("Generating products...");
        Product[] products = generateProducts(size);

        Product[] sortedProducts = Arrays.copyOf(products, products.length);
        sortById(sortedProducts);

        // Test cases
        int bestCaseId = products[0].getProductId();          // first element
        int avgCaseId = products[size / 2].getProductId();    // middle
        int worstCaseId = -1;                                 // not found

        long seqBest = measureSequential(products, bestCaseId);
        long binBest = measureBinary(sortedProducts, bestCaseId);

        long seqAvg = measureSequential(products, avgCaseId);
        long binAvg = measureBinary(sortedProducts, avgCaseId);

        long seqWorst = measureSequential(products, worstCaseId);
        long binWorst = measureBinary(sortedProducts, worstCaseId);

        // Output
        System.out.println("\nSearch Performance Results\n");

        System.out.println("Best Case:");
        System.out.println("Sequential = " + seqBest + " ns");
        System.out.println("Binary = " + binBest + " ns\n");

        System.out.println("Average Case:");
        System.out.println("Sequential = " + seqAvg + " ns");
        System.out.println("Binary = " + binAvg + " ns\n");

        System.out.println("Worst Case:");
        System.out.println("Sequential = " + seqWorst + " ns");
        System.out.println("Binary = " + binWorst + " ns");
    }
}