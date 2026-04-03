package Search_Optimization;

/**
 * Provides search algorithms for finding products in an array.
 *
 * <p>This class contains three search implementations:</p>
 * <ul>
 *   <li>Sequential search by product ID</li>
 *   <li>Binary search by product ID (requires sorted array)</li>
 *   <li>Sequential search by product name (case-insensitive)</li>
 * </ul>
 *
 * @author Ilodigwe Nzube
 * @version 1.0
 * @since 2024
 */
public class ProductSearch {

    // 1. Sequential Search by ID
    /**
     * Performs a sequential (linear) search to find a product by its ID.
     *
     * <p>This method iterates through the array from index 0 to length-1,
     * comparing each product's ID with the target ID. It works on both
     * sorted and unsorted arrays, but has O(n) time complexity.</p>
     *
     * @param products the array of products to search through
     * @param targetId the product ID to search for
     * @return the Product with the matching ID, or null if not found
     */
    public static Product sequentialSearchById(Product[] products, int targetId) {
        for (int i = 0; i < products.length; i++) {
            if (products[i].getProductId() == targetId) {
                return products[i];
            }
        }
        return null; // If not found
    }

    // 2. Binary Search by ID (array must be sorted by productId)
    /**
     * Performs a binary search to find a product by its ID.
     *
     * <p>This method requires that the input array is already sorted by
     * productId in ascending order. It repeatedly divides the search
     * interval in half, achieving O(log n) time complexity.</p>
     *
     * @param products the array of products to search through (must be sorted by productId)
     * @param targetId the product ID to search for
     * @return the Product with the matching ID, or null if not found
     */
    public static Product binarySearchById(Product[] products, int targetId) {
        int left = 0;
        int right = products.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (products[mid].getProductId() == targetId) {
                return products[mid];
            } else if (products[mid].getProductId() < targetId) {
                left = mid + 1;   // Search in the right half
            } else {
                right = mid - 1;  // Search in the left half
            }
        }

        return null; // If not found
    }

    // 3. Sequential Search by Name (names are unsorted)
    /**
     * Performs a case-insensitive sequential search to find a product by its name.
     *
     * <p>This method iterates through the array and compares each product's
     * name with the target name using case-insensitive comparison.
     * Time complexity is O(n) and works on unsorted data.</p>
     *
     * @param products   the array of products to search through
     * @param targetName the product name to search for (case-insensitive)
     * @return the Product with the matching name, or null if not found
     */
    public static Product searchByName(Product[] products, String targetName) {
        for (int i = 0; i < products.length; i++) {
            if (products[i].getProductName().equalsIgnoreCase(targetName)) {
                return products[i];
            }
        }
        return null; // If not found
    }
}