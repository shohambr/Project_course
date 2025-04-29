package DomainLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShoppingBag {
    private String storeId;
    private Map<String, Integer> products;       //String repressent product Id

    public ShoppingBag( String storeId) {
        this.storeId = storeId;
        this.products = new HashMap<String, Integer>();
    }



    public String getStoreId() {return storeId;}

    public Map<String, Integer> getProducts() { return products; }

    public void addProduct(String productId , Integer quantity) {
        boolean found = false;
        for (String product : products.keySet()) {
            if (productId.equals(product)) {
                products.put(product, Integer.valueOf(products.get(product) + quantity));
                found = true;
            }
        }
        if (!found) {
            products.put(productId, quantity);
        }
    }

    public boolean removeProduct(String productId , Integer quantity) {
        if (products.containsKey(productId)) {
            int currentQuantity = products.get(productId);
            if (currentQuantity > quantity) {
                products.put(productId, Integer.valueOf(currentQuantity - quantity));
            } else if (currentQuantity == quantity) {
                products.remove(productId);
            } else {
                return false;
            }
        }
        return true;
    }
    

    public void sold() {
        for (String product : products.keySet()) {
            products.put(product, 0);
        }
    }
}
