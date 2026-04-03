package Search_Optimization;

/**
 * Represents a product in the search and optimization system.
 *
 * <p>A Product contains basic information including a unique identifier,
 * product name, category, price, and stock quantity. This class is used
 * by the HybridProductCatalog and search algorithms for product management
 * and retrieval operations.</p>
 *
 * @author Ilodigwe Nzube
 * @version 1.0
 * @since 2024
 */
public class Product {

    // Attributes
    private int productId;       // Unique identifier
    private String productName;
    private String category;
    private double price;
    private int stockQuantity;

    /**
     * Constructs a new Product with the specified details.
     *
     * @param productId      the unique identifier for the product
     * @param productName    the name of the product
     * @param category       the category the product belongs to
     * @param price          the price of the product
     * @param stockQuantity  the available stock quantity of the product
     */
    public Product(int productId, String productName, String category, double price, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getter methods
    /**
     * Returns the unique identifier of the product.
     *
     * @return the product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Returns the name of the product.
     *
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Returns the category of the product.
     *
     * @return the product category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the price of the product.
     *
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the available stock quantity of the product.
     *
     * @return the stock quantity
     */
    public int getStockQuantity() {
        return stockQuantity;
    }

    // Setter methods
    /**
     * Sets the name of the product.
     *
     * @param productName the new product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Sets the category of the product.
     *
     * @param category the new product category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the new product price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the available stock quantity of the product.
     *
     * @param stockQuantity the new stock quantity
     */
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}