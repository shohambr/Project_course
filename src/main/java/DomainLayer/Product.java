package DomainLayer;

public class Product {
    private String id;
    private String storeId;
    private String name;
    private String description;
    private int price;
    private int quantity;

    public Product(String id, String storeId, String name, String description, int price , int quantity) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity; // was before 0 changed to quantity, so the product holds the quantity of itself aswell
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

    //setters
    public void setId(String id) {this.id = id; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public void changeName(String name) { this.name = name; }
    public void setDescription(String description) {this.description = description;}
    public void changePrice(int price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String toString() {
        return "Product{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

}