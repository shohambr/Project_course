package DomainLayer;
import java.util.UUID;

public class Product {
    private String id = UUID.randomUUID().toString();
    private String storeId;
    private String name;
    private String description;
    private int price;
    private int quantity;
    private double rating;
    private String category;

    public Product(String id, String storeId, String name, String description, int price , int quantity, double rating, String category) {
        if(quantity > 0) {
            this.storeId = storeId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.rating = rating;
            this.category = category;
        }
        else {
            throw new IllegalArgumentException("Product must have at least 1 quantity");
        }
    }

    public Product() {
        //needed for Jackson
    }
    
    //getters
    public String getId() {
        return id;
    }
    public String getStoreId() {return storeId; }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getPrice() {
        return price;
    }
    public int getQuantity() { return quantity; }
    public double getRating() { return rating; }
    public String getCategory() { return category; }

    //setters
    public synchronized void setId(String id) {this.id = id; }
    public synchronized void setStoreId(String storeId) { this.storeId = storeId; }
    public synchronized void changeName(String name) { this.name = name; }
    public synchronized void setDescription(String description) {this.description = description;}
    public synchronized void changePrice(int price) { this.price = price; }
    public synchronized void setQuantity(int quantity) { this.quantity = quantity; }
    public synchronized void setRating(double rating) { this.rating = rating; }
    public synchronized void setCategory(String category) { this.category = category; }

    public String toString() {
        return "Product{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", rating=" + rating +
                ", category=" + category +
                '}';
    }

}