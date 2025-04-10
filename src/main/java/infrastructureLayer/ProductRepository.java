package infrastructureLayer;
import DomainLayer.IProductRepository;
import DomainLayer.Product;

import java.util.*;

public class ProductRepository implements IProductRepository {

    private final HashMap <String, Product> products;

    private ProductRepository() {
        this.products = new HashMap<String, Product>();
    }

    public void save(Product product) {
        String key = product.getId();
        products.put(key, product);
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public void deleteById(String id) {
        products.remove(id);
    }
}
