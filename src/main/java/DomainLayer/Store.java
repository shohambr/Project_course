package DomainLayer;
import DomainLayer.Roles.Guest;

import java.util.*;

public class Store {
    private String name;
    private List<User> users = new ArrayList<>();
    private List<Product> allProducts = new ArrayList<>();
    private int nextUserId = 1;
    private int nextProductId = 1;

    public Store(String name) {
        this.name = name;
    }

//    public User registerUser(String name, String username, String password, Role role, ShoppingCart sc) {
//        User user = new Guest();
//        users.add(user);
//        return user;
//    }

    public Product createProduct(String Iname, String description, int price, int quantity) {
        Product product = new Product("" + nextProductId++ , name , Iname, description, price, quantity);
        allProducts.add(product);
        return product;
    }

    public void assignProductToUser(User user, Product product) {
        user.addProduct(this , product);
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Store Name: ").append(name).append("\n");

        sb.append("\nUsers:\n");
        for (User user : users) {
            sb.append(user.toString()).append("\n");
        }

        sb.append("\nAll Products in Store:\n");
        for (Product product : allProducts) {
            sb.append(product.toString()).append("\n");
        }

        return sb.toString();
    }
    
    public String getName() { return name; }

    public void close() {
        //to implement
        //implementation should include a way to reopen
    }

    public void reOpen() {
        //to implement
    }

    public void changeProductInventory(int productID, int quantity) {
        //to implement
    }

    public void changeProductPrice(int productID, int price) {
        //to implement
    }

    public void changeProductDescription(int productID, String description) {
        //to implement
    }

    public void removeProductFromInventory(int productID) {
        //to implement
    }
}
