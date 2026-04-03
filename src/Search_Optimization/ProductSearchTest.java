package Search_Optimization;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for verifying the correctness of product search implementations.
 *
 * <p>This class tests three main search functionalities:</p>
 * <ul>
 *   <li>Sequential search by product ID</li>
 *   <li>Binary search by product ID</li>
 *   <li>Hybrid catalog search by name and sorted insertion</li>
 * </ul>
 *
 * @author Ilodigwe Nzube
 * @version 1.0
 * @since 2024
 */
public class ProductSearchTest {

    /**
     * Tests the sequentialSearchById method for both found and not found cases.
     *
     * <p>Verifies that:</p>
     * <ul>
     *   <li>A product with an existing ID is correctly found and returned</li>
     *   <li>A non-existent ID returns null</li>
     * </ul>
     */
    @Test
    void testSequentialSearchById() {
        // Create test data array with 5 products having IDs 10, 20, 30, 40, 50
        Product[] products = new Product[5];
        products[0] = new Product(10, "A", "Cat1", 100, 5);
        products[1] = new Product(20, "B", "Cat2", 200, 10);
        products[2] = new Product(30, "C", "Cat3", 300, 15);
        products[3] = new Product(40, "D", "Cat4", 400, 20);
        products[4] = new Product(50, "E", "Cat5", 500, 25);

        // Test case 1: Search for an existing product (ID = 30)
        Product p = ProductSearch.sequentialSearchById(products, 30);
        assertNotNull(p);                     // Should not be null
        assertEquals(30, p.getProductId());   // Should have matching ID

        // Test case 2: Search for a non-existent product (ID = 99)
        assertNull(ProductSearch.sequentialSearchById(products, 99));
    }

    /**
     * Tests the binarySearchById method for both found and not found cases.
     *
     * <p>Note: This test assumes the input array is already sorted by product ID.
     * Binary search requires sorted data to function correctly.</p>
     *
     * <p>Verifies that:</p>
     * <ul>
     *   <li>A product with an existing ID is correctly found using binary search</li>
     *   <li>A non-existent ID returns null</li>
     * </ul>
     */
    @Test
    void testBinarySearchById() {
        // Create sorted test data array with IDs 10, 20, 30, 40, 50
        Product[] products = new Product[5];
        products[0] = new Product(10, "A", "Cat1", 100, 5);
        products[1] = new Product(20, "B", "Cat2", 200, 10);
        products[2] = new Product(30, "C", "Cat3", 300, 15);
        products[3] = new Product(40, "D", "Cat4", 400, 20);
        products[4] = new Product(50, "E", "Cat5", 500, 25);

        // Array must be sorted by ID for binary search to work correctly
        // Test case 1: Search for an existing product (ID = 40)
        Product p = ProductSearch.binarySearchById(products, 40);
        assertNotNull(p);                     // Should not be null
        assertEquals(40, p.getProductId());   // Should have matching ID

        // Test case 2: Search for a non-existent product (ID = 99)
        assertNull(ProductSearch.binarySearchById(products, 99));
    }

    /**
     * Tests the name-based search functionality in the HybridProductCatalog.
     *
     * <p>Verifies that:</p>
     * <ul>
     *   <li>Products added to the catalog can be found by their exact name</li>
     *   <li>Searching for a non-existent name returns null</li>
     * </ul>
     */
    @Test
    void testSearchByNameInHybridCatalog() {
        // Create a catalog with capacity for 10 products
        HybridProductCatalog catalog = new HybridProductCatalog(10);

        // Create two test products
        Product p1 = new Product(1, "Alpha", "Cat1", 100, 5);
        Product p2 = new Product(2, "Beta", "Cat2", 200, 10);

        // Add products to the catalog
        catalog.addProduct(p1);
        catalog.addProduct(p2);

        // Test case 1: Search for an existing product by name "Alpha"
        Product found = catalog.searchByName("Alpha");
        assertNotNull(found);                       // Should not be null
        assertEquals("Alpha", found.getProductName()); // Name should match

        // Test case 2: Search for a non-existent product name "Gamma"
        assertNull(catalog.searchByName("Gamma"));
    }

    /**
     * Tests that addProduct correctly maintains sorted order by product ID.
     *
     * <p>Products are added in non-sorted order (20, then 10, then 30).
     * This test verifies that after insertion, searchById correctly finds
     * all products regardless of insertion order.</p>
     *
     * <p>Verifies that:</p>
     * <ul>
     *   <li>Products are internally stored in sorted order by ID</li>
     *   <li>Binary search by ID works correctly after multiple insertions</li>
     * </ul>
     */
    @Test
    void testAddProductMaintainsSortedOrder() {
        // Create a catalog with capacity for 5 products
        HybridProductCatalog catalog = new HybridProductCatalog(5);

        // Add products in non-sorted order: 20, then 10, then 30
        Product p1 = new Product(20, "B", "Cat2", 200, 10);
        Product p2 = new Product(10, "A", "Cat1", 100, 5);
        Product p3 = new Product(30, "C", "Cat3", 300, 15);

        catalog.addProduct(p1);  // Insert ID 20
        catalog.addProduct(p2);  // Insert ID 10 (should shift ID 20 to the right)
        catalog.addProduct(p3);  // Insert ID 30 (should go to the end)

        // Verify that all products can be found via binary search by ID
        // This confirms the array remains sorted after each insertion
        assertEquals(10, catalog.searchById(10).getProductId());  // ID 10 should be at index 0
        assertEquals(20, catalog.searchById(20).getProductId());  // ID 20 should be at index 1
        assertEquals(30, catalog.searchById(30).getProductId());  // ID 30 should be at index 2
    }
}