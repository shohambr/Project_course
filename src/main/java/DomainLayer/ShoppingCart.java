package DomainLayer;
import java.util.*;

public class ShoppingCart {
    private String userId;
    private List<ShoppingBag> shoppingBags;

    public ShoppingCart(String userId) {
        this.userId = userId;
        this.shoppingBags = new ArrayList<ShoppingBag>();
    }

    public ShoppingCart() {
        this.userId = "";
        this.shoppingBags = new ArrayList<ShoppingBag>();
    }

    public void addProduct(Store store, Product product) {

        boolean found = false;
        for (ShoppingBag shoppingBag : shoppingBags) {
            if (shoppingBag.getStoreId().equals(store.getId())) {
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

    public boolean removeProduct(String storeId, String productId , int amount) {
        for (ShoppingBag shoppingBag : shoppingBags) {
            if (shoppingBag.getStoreId().equals(storeId)) {
                if (shoppingBag.removeProduct(productId , amount)) {
                    if (shoppingBag.getProducts().isEmpty()) {
                        shoppingBags.remove(shoppingBag);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public List<ShoppingBag> getShoppingBags() {return shoppingBags;}

    public String getUserId() { return userId; }

    public boolean availablePurchaseCart() {
        for (ShoppingBag shoppingBag : shoppingBags) {
            if(!shoppingBag.availablePurchaseShoppingBag()){
                return false;
            }
        }
        return true;
    }

    public double calculatePurchaseCart() {
        if(availablePurchaseCart()) {
            double price = 0;
            for (ShoppingBag shoppingBag : shoppingBags) {
                price = price + shoppingBag.calculatePurchaseShoppingBag();
                shoppingBags.remove(shoppingBag);
            }
            return price;
        }
        return -1;
    }

    public void sold (){
        for (ShoppingBag shoppingBag : shoppingBags) {
            shoppingBag.sold();
        }
    }

}
