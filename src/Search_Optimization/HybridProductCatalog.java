package Search_Optimization;

import java.util.*;

/**
 * A hybrid catalog system that maintains products in two index structures simultaneously.
 *
 * <p>This class provides O(log n) search by product ID using binary search on a sorted array,
 * and O(1) search by product name using a HashMap index. Products are automatically
 * inserted in sorted order by ID while also being added to the name index.</p>
 *
 * <p><b>Time Complexities:</b></p>
 * <ul>
 *   <li>addProduct(): O(n) due to array shifting for sorted insertion</li>
 *   <li>searchById(): O(log n) using binary search</li>
 *   <li>searchByName(): O(1) average case using HashMap</li>
 * </ul>
 *
 * @author Ilodigwe Nzube
 * @version 1.0
 * @since 2024
 */
public class HybridProductCatalog {

    /** Sorted array of products indexed by product ID */
    private Product[] products;

    /** HashMap index for O(1) lookup by product name */
    private Map<String, Product> nameIndex;

    /** Current number of products in the catalog */
    private int size;

    /**
     * Constructs an empty HybridProductCatalog with the specified capacity.
     *
     * @param capacity the maximum number of products the catalog can hold
     */
    public HybridProductCatalog(int capacity) {
        products = new Product[capacity];
        nameIndex = new HashMap<>();
        size = 0;
    }

    /**
     * Adds a new product to the catalog while maintaining sorted order by ID.
     *
     * <p>The product is inserted into the products array at the correct position
     * to keep the array sorted by product ID. All elements to the right are
     * shifted one position. The product is also added to the name index for
     * fast name-based lookups.</p>
     *
     * @param newProduct the product to add to the catalog
     */
    public void addProduct(Product newProduct) {
        // Find insertion position using binary search on the sorted portion of the array
        int pos = Arrays.binarySearch(products, 0, size, newProduct,
                Comparator.comparingInt(Product::getProductId));

        // binarySearch returns (-insertionPoint - 1) if not found
        // Convert to the actual insertion index
        if (pos < 0) pos = -pos - 1;

        // Shift all elements to the right to make room for the new product
        for (int i = size; i > pos; i--) {
            products[i] = products[i - 1];
        }

        // Insert the new product at the correct position
        products[pos] = newProduct;
        size++;

        // Add to the name index for O(1) lookup by name
        nameIndex.put(newProduct.getProductName(), newProduct);
    }

    /**
     * Searches for a product by its unique ID using binary search.
     *
     * <p>This method takes advantage of the fact that the products array
     * is always maintained in sorted order by product ID.</p>
     *
     * @param targetId the product ID to search for
     * @return the Product with the matching ID, or null if not found
     */
    public Product searchById(int targetId) {
        int left = 0, right = size - 1;

        // Standard iterative binary search
        while (left <= right) {
            int mid = (left + right) / 2;

            if (products[mid].getProductId() == targetId) {
                return products[mid];           // Found at mid position
            } else if (products[mid].getProductId() < targetId) {
                left = mid + 1;                  // Search right half
            } else {
                right = mid - 1;                 // Search left half
            }
        }
        return null;  // Product not found
    }

    /**
     * Searches for a product by its exact name using the HashMap index.
     *
     * <p>This provides O(1) average-case lookup time, making it much faster
     * than searching through the array for name-based queries.</p>
     *
     * @param targetName the exact product name to search for
     * @return the Product with the matching name, or null if not found
     */
    public Product searchByName(String targetName) {
        // getOrDefault returns null if the key doesn't exist
        return nameIndex.getOrDefault(targetName, null);
    }

    /**
     * Returns the current number of products in the catalog.
     *
     * @return the number of products currently stored
     */
    public int getSize() {
        return size;
    }
}