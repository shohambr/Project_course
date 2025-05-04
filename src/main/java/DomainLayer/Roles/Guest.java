package DomainLayer.Roles;

import java.util.UUID;

import DomainLayer.Product;
import DomainLayer.ShoppingBag;
import DomainLayer.ShoppingCart;
import DomainLayer.Store;
import io.micrometer.observation.Observation.Event;
import ServiceLayer.EventLogger;

public class Guest {

    protected String id = UUID.randomUUID().toString();
    protected ShoppingCart shoppingCart = new ShoppingCart((id));
    protected static int counter = 0;
    protected String username = "Guest"+ counter++;
    protected Boolean cartReserved = false;


    public Guest() {
    }


    public void addProduct(String storeId, String productId , Integer quantity) {
        boolean found = false;
        for (ShoppingBag shoppingBag : shoppingCart.getShoppingBags()) {
            if (shoppingBag.getStoreId().equals(storeId)) {
                shoppingBag.addProduct(productId , quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            ShoppingBag shoppingBag = new ShoppingBag(storeId);
            shoppingBag.addProduct(productId , quantity);
            shoppingCart.getShoppingBags().add(shoppingBag);
        }
    }

    public void removeProduct(String storeId, String productId , Integer quantity) {
        for (ShoppingBag shoppingBag : shoppingCart.getShoppingBags()) {
            if (shoppingBag.getStoreId().equals(storeId)) {
                boolean found = shoppingBag.removeProduct(productId , quantity);
                if (found) {
                    if (shoppingBag.getProducts().isEmpty()) {
                        shoppingCart.getShoppingBags().remove(shoppingBag);
                    }
                    break;
                } else {
                    EventLogger.logEvent(productId, "Product not found in cart");
                    throw new IllegalArgumentException("Product not found in cart");
                }
            }
        }
    }

    public String getID() {
        return this.id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setCartReserved(Boolean cartReserved) {
        this.cartReserved = cartReserved;
    }


    public Boolean getCartReserved() {
        return cartReserved;
    }

}
