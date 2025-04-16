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

    public String getStoreName() {
        return store.getName();
    }

    public Map<Product, Integer> getProducts() { return products; }

    public void addProduct(Product productToAdd) {
        boolean found = false;
        for (Product product : products.keySet()) {
            if (productToAdd.getId() == product.getId()) {
                products.put(product, products.get(product) + 1);
                found = true;
            }
        }

        if (!found) {
            products.put(productToAdd, 1); //needs update to use with database

        }
    }

    public void removeProduct(Product productToRemove) {
        for (Product product : products.keySet()) {
            if (productToRemove.getId() == product.getId()) {
                products.put(product, products.get(product) - 1);
                if (products.get(product) == 0) {
                    products.remove(product);
                }
            }
        }

    }

    public void removeAllProducts(Product productToRemove) {
        //needs update to use with database
        products.remove(productToRemove);
    }
}
