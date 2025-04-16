package DomainLayer;
import ServiceLayer.PaymentService;
import ServiceLayer.ProductService;

import java.util.*;

public class Store {
    private String id;
    private User owner;
    private PurchasePolicy purchasePolicy;
    private List<User> users = new ArrayList<>();
    private Map<Product, Integer> products = new HashMap<>();
    private ProductService productService;


    public Store(User owner) {
        this.owner = owner;
    }

    /**
     * use this function to detect if the store is open now so the logic is not depended on the boolean itself.
     * for example a store that despite being open would like to automatically open and close in certain hours.
     * @return a boolean that says if the store is open right now
     */
    public boolean isOpenNow() {
        return openNow;
    }
    public void openTheStore() {
        openNow = true;
    }
    public void closeTheStore() {
        openNow = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    public Boolean registerUser(User user) {
        if(users.contains(user)) {
            return false;
        }
        users.add(user);
        return true;
    }

    public boolean increaseProduct(Product product, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        if (!products.containsKey(product)) {
            return false;
        }

        int currentQuantity = products.get(product);
        products.put(product, Integer.valueOf(currentQuantity + quantity));
        return true;
    }


    public boolean removeProduct(Product product, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        if (!products.containsKey(product)) {
            return false;
        }
        if (quantity > products.get(product)) {
            return false;
        }

        int updatedQuantity = products.get(product) - quantity;
        if (updatedQuantity == 0) {
            products.remove(product);
        } else {
            products.put(product, updatedQuantity);
        }
        productService.decreaseQuantity(product.getId(), quantity);       //Changed according productService implementation
        return true;
    }

    public int sellProduct(Product product, int quantity) {
        if (quantity <= 0) {
            return -1;
        }
        if (!products.containsKey(product)) {
            return -1;
        }
        if (quantity > products.get(product)) {
            return -1;
        }

        int updatedQuantity = products.get(product) - quantity;
        if (updatedQuantity == 0) {
            products.remove(product);
        } else {
            products.put(product, updatedQuantity);
        }
        productService.decreaseQuantity(product.getId(), quantity);       //Changed according productService implementation
        return product.getPrice() * quantity * purchasePolicy;            //got to decide how purchase policy works
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nUsers:\n");
        for (User user : users) {
            sb.append(user.toString()).append("\n");
        }

        sb.append("\nAll Products in Store:\n");
        for (Product product : products.keySet()) {
            sb.append(product.toString()).append("\n");
        }

        return sb.toString();
    }

}
