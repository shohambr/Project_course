package infrastructureLayer;
import DomainLayer.IProductRepository;
import DomainLayer.Product;

import java.util.*;

public class ProductRepository implements IProductRepository {
    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, Product> productsByName = new HashMap<>();

    public synchronized void save(Product product) {
        products.put(product.getId(), product);
        productsByName.put(product.getName(), product);
    }

    public synchronized Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public synchronized Optional<Product> findByName(String name) {
        return Optional.ofNullable(productsByName.get(name));
    }

    public synchronized List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public synchronized void deleteById(String id) {
        Product product = products.remove(id);
        if (product != null) {
            productsByName.remove(product.getName());
        }
    }
}
