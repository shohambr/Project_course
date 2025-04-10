package DomainLayer;
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

    public User registerUser(String name, String username, String password, Role role, ShoppingCart sc) {
        User user = new User(nextUserId++, name, username, password, role, sc);
        users.add(user);
        return user;
    }

    public Product createProduct(String name, String description, int price, int quantity) {
        Product product = new Product(nextProductId++, name, description, price, quantity);
        allProducts.add(product);
        return product;
    }

    public void assignProductToUser(User user, Product product) {
        user.addProduct(product);
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
        
}
