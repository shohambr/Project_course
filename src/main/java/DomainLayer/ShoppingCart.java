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

    public boolean removeProduct(Store store, Product product) {
        for (ShoppingBag shoppingBag : shoppingBags) {
            if (shoppingBag.getStoreId().equals(store.getId())) {
                if (shoppingBag.removeProduct(product)) {
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
            }
            return price;
        }
        return -1;
    }

    public Map<Store, Double> calculatePaymentStore() {
        Map<Store, Double> storePayment = new HashMap<Store, Double>();
        for(ShoppingBag shoppingBag: shoppingBags) {
            storePayment.put(shoppingBag.getStore(), shoppingBag.calculatePurchaseShoppingBag());
        }
        return storePayment;
    }


    public void sold (){
        for (ShoppingBag shoppingBag : shoppingBags) {
            shoppingBag.sold();
        }
        shoppingBags = new ArrayList<ShoppingBag>();
    }

}
