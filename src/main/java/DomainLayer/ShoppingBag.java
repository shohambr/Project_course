package DomainLayer;

import java.util.ArrayList;
import java.util.List;

class ProductAmount {
    public Product product;
    public int amount;

    public ProductAmount() {}
}

public class ShoppingBag {
    private String storeName;
    private List<ProductAmount> products;

    public ShoppingBag(Store store) {
        this.storeName = store.getName();
        this.products = new ArrayList<ProductAmount>();
    }

    public String getStoreName() {
        return storeName;
    }

    public List<ProductAmount> getProducts() { return products; }

    public void addProduct(Product product) {
        boolean found = false;
        for (ProductAmount productAmount : products) {
            if (product.getId() == productAmount.product.getId()) {
                productAmount.amount = productAmount.amount + 1;
                found = true;
            }
        }

        if (!found) {
            ProductAmount newProductAmount = new ProductAmount();
            newProductAmount.product = product;
            newProductAmount.amount = 1;
            products.add(newProductAmount); //needs update to use with database

        }
    }

    public void removeProduct(Product product) {
        for (ProductAmount productAmount : products) {
            if (product.getId() == productAmount.product.getId()) {
                productAmount.amount = productAmount.amount - 1;
                if (productAmount.amount == 0) {
                        products.remove(productAmount); //needs update to use with database
                    }
                }
            }
    }

    public void removeAllProducts(Product product) {
        //needs update to use with database
        products.removeIf(productAmount -> product.getId() == productAmount.product.getId());
    }
}
