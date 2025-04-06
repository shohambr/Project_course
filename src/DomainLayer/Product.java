package DomainLayer;

import java.util.*;

public class Product {
    private int id;
    private String name;
    private String description;
    private int price;
    private int quantity;

    public Product(int id, String name, String description, int price , int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = 0;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void changeName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void changeDescription(String description) {
        this.description = description;
    }
    public int getPrice() {
        return price;
    }
    public void changePrice(int price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void decrementQuantity() {
        this.quantity--;
    }
    public String createProductHistory(){
        String productHistory="";
        productHistory.concat("name: ");
        productHistory.concat(this.name);
        productHistory.concat("\n");
        productHistory.concat("description: ");
        productHistory.concat(this.description);
        productHistory.concat("\n");
        productHistory.concat("price: ");
        productHistory.concat(String.valueOf(this.price));
        return productHistory;
    }

}