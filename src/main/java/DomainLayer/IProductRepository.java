package DomainLayer;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    void save(Product product);
    Optional<Product> findById(String id);
    Optional<Product> findByName(String name);
    List<Product> findAll();
    void deleteById(String id);
}
