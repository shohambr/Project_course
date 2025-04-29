package DomainLayer.Roles;

import java.util.UUID;

import DomainLayer.Product;
import DomainLayer.ShoppingBag;
import DomainLayer.ShoppingCart;
import DomainLayer.Store;
import ServiceLayer.UserService;

public class Guest {

    protected String id = UUID.randomUUID().toString();
    protected ShoppingCart shoppingCart = new ShoppingCart((id));
    protected String myToken;
    protected String name = "Guest";


    public Guest() {
    }

    public Guest(String token) {
        this.myToken = token;
    }


    public void addProduct(String storeId, String productId , Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        boolean found = false;
        for (ShoppingBag shoppingBag : shoppingCart.getShoppingBags()) {
            if (shoppingBag.getStoreId().equals(storeId)) {
                found = true;
                shoppingBag.addProduct(productId , quantity);
            }
        }
        if (!found) {
            ShoppingBag shoppingBag = new ShoppingBag(storeId);
            shoppingBag.addProduct(productId , quantity);
            shoppingCart.getShoppingBags().add(shoppingBag);
        }
    }

    public void removeProduct(String storeId, String productId , Integer quantity) {
        boolean found = false;
        for (ShoppingBag shoppingBag : shoppingCart.getShoppingBags()) {
            if (shoppingBag.getStoreId().equals(storeId)) {
                found = shoppingBag.removeProduct(productId , quantity);
                if (shoppingBag.getProducts().isEmpty()) {
                    shoppingCart.getShoppingBags().remove(shoppingBag);
                }
            }
        }
    }

    public String getToken() {
        return myToken;
    }
    public String getID() {
        return this.id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

}
