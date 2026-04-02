import java.util.*;

public class HybridProductCatalog {
    private Product[] products;          // sorted by ID
    private Map<String, Product> nameIndex;
    private int size;                    // current number of products

    public HybridProductCatalog(int capacity) {
        products = new Product[capacity];
        nameIndex = new HashMap<>();
        size = 0;
    }

    // Add new product while maintaining sorted ID array
    public void addProduct(Product newProduct) {
        int pos = Arrays.binarySearch(products, 0, size, newProduct,
                Comparator.comparingInt(Product::getProductId));
        if (pos < 0) pos = -pos - 1;

        for (int i = size; i > pos; i--) {
            products[i] = products[i - 1];
        }
        products[pos] = newProduct;
        size++;

        nameIndex.put(newProduct.getProductName(), newProduct);
    }

    // Binary search by ID
    public Product searchById(int targetId) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (products[mid].getProductId() == targetId) return products[mid];
            else if (products[mid].getProductId() < targetId) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    // Fast search by name
    public Product searchByName(String targetName) {
        return nameIndex.getOrDefault(targetName, null);
    }

    public int getSize() {
        return size;
    }
}