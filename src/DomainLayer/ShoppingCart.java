package DomainLayer;
import java.util.*;

public class ShoppingCart {
    private int userId;
    private List<ShoppingBag> shoppingBags;

    public ShoppingCart(int userId) {
        this.userId = userId;
        this.shoppingBags = new ArrayList<ShoppingBag>();
    }

    public void addProduct(Store store, Product product) {

        boolean found = false;
        for (ShoppingBag shoppingBag : shoppingBags) {
            if (shoppingBag.getStoreName().equals(store.getName())) {
                shoppingBag.addProduct(product);
                found = true;
            }
        }

        if (!found) {
            ShoppingBag newShoppingBag = new ShoppingBag(store);
            newShoppingBag.addProduct(product);
            shoppingBags.add(newShoppingBag);

        }
    }

    public void removeProduct(Store store, Product product) {
        for (ShoppingBag shoppingBag : shoppingBags) {
            if (shoppingBag.getStoreName().equals(store.getName())) {
                shoppingBag.removeProduct(product);
                if (shoppingBag.getProducts().isEmpty()) {
                    shoppingBags.remove(shoppingBag);
                }
            }
        }
    }

    public int getUserId() { return userId; }

}
