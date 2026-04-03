package Search_Optimization;

public class ProductSearch {

    // 1. Sequential Search by ID
    public static Product sequentialSearchById(Product[] products, int targetId) {
        for (int i = 0; i < products.length; i++) {
            if (products[i].getProductId() == targetId) {
                return products[i];
            }
        }
        return null; // If not found
    }

    // 2. Binary Search by ID (array must be sorted by productId)
    public static Product binarySearchById(Product[] products, int targetId) {
        int left = 0;
        int right = products.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (products[mid].getProductId() == targetId) {
                return products[mid];
            } else if (products[mid].getProductId() < targetId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return null; // If not found
    }

    // 3. Sequential Search by Name (names are unsorted)
    public static Product searchByName(Product[] products, String targetName) {
        for (int i = 0; i < products.length; i++) {
            if (products[i].getProductName().equalsIgnoreCase(targetName)) {
                return products[i];
            }
        }
        return null; // If not found
    }
}