package DomainLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShoppingBag {
    private Store store;
    private Map<Product, Integer> products;

    public ShoppingBag(Store store) {
        this.store = store;
        this.products = new HashMap<Product, Integer>();
    }

    public String getStoreId() {return store.getId();}

    public Map<Product, Integer> getProducts() { return products; }

    public void addProduct(Product productToAdd) {
        boolean found = false;
        for (Product product : products.keySet()) {
            if (productToAdd.getId().equals(product.getId())) {
                products.put(product, Integer.valueOf(products.get(product) + 1));
                found = true;
            }
        }

        if (!found) {
            products.put(productToAdd, Integer.valueOf(1)); //needs update to use with database
        }
    }

    public boolean removeProduct(String productId, int amount) {
        boolean found = false;
        for (Map.Entry<Product, Integer> product : products.entrySet()) {
            if (product.getKey().getId().equals(productId)) {
                if (product.getValue() > amount) {
                    products.put(product.getKey(), Integer.valueOf(product.getValue() - amount));
                } else {
                    products.remove(product.getKey());
                }
                found = true;
            }
        }
        return found;
    }


    public boolean availablePurchaseShoppingBag() {
        for (Map.Entry<Product, Integer> product : products.entrySet()) {
            if(!store.availableProduct(product.getKey(), product.getValue())){
                return false;
            }
        }
        return true;
    }

    public double calculatePurchaseShoppingBag() {

        double price = 0;
        for (Map.Entry<Product, Integer> product : products.entrySet()) {
            price = price + store.calculateProduct(product.getKey(), product.getValue());
        }
        return price;
    }

    public void sold() {
        for (Map.Entry<Product, Integer> product : products.entrySet()) {
            store.decreaseProduct(product.getKey(), product.getValue());
        }
    }
}
