import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductSearchTest {

    @Test
    void testSequentialSearchById() {
        Product[] products = new Product[5];
        products[0] = new Product(10, "A", "Cat1", 100, 5);
        products[1] = new Product(20, "B", "Cat2", 200, 10);
        products[2] = new Product(30, "C", "Cat3", 300, 15);
        products[3] = new Product(40, "D", "Cat4", 400, 20);
        products[4] = new Product(50, "E", "Cat5", 500, 25);

        // Test found
        Product p = ProductSearch.sequentialSearchById(products, 30);
        assertNotNull(p);
        assertEquals(30, p.getProductId());

        // Test not found
        assertNull(ProductSearch.sequentialSearchById(products, 99));
    }

    @Test
    void testBinarySearchById() {
        Product[] products = new Product[5];
        products[0] = new Product(10, "A", "Cat1", 100, 5);
        products[1] = new Product(20, "B", "Cat2", 200, 10);
        products[2] = new Product(30, "C", "Cat3", 300, 15);
        products[3] = new Product(40, "D", "Cat4", 400, 20);
        products[4] = new Product(50, "E", "Cat5", 500, 25);

        // Array must be sorted by ID
        Product p = ProductSearch.binarySearchById(products, 40);
        assertNotNull(p);
        assertEquals(40, p.getProductId());

        assertNull(ProductSearch.binarySearchById(products, 99));
    }

    @Test
    void testSearchByNameInHybridCatalog() {
        HybridProductCatalog catalog = new HybridProductCatalog(10);
        Product p1 = new Product(1, "Alpha", "Cat1", 100, 5);
        Product p2 = new Product(2, "Beta", "Cat2", 200, 10);

        catalog.addProduct(p1);
        catalog.addProduct(p2);

        // Search existing
        Product found = catalog.searchByName("Alpha");
        assertNotNull(found);
        assertEquals("Alpha", found.getProductName());

        // Search non-existing
        assertNull(catalog.searchByName("Gamma"));
    }

    @Test
    void testAddProductMaintainsSortedOrder() {
        HybridProductCatalog catalog = new HybridProductCatalog(5);
        Product p1 = new Product(20, "B", "Cat2", 200, 10);
        Product p2 = new Product(10, "A", "Cat1", 100, 5);
        Product p3 = new Product(30, "C", "Cat3", 300, 15);

        catalog.addProduct(p1);
        catalog.addProduct(p2);
        catalog.addProduct(p3);

        // Check binary search finds them correctly
        assertEquals(10, catalog.searchById(10).getProductId());
        assertEquals(20, catalog.searchById(20).getProductId());
        assertEquals(30, catalog.searchById(30).getProductId());
    }
}