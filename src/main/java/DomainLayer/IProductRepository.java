package DomainLayer;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {

    void save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    void deleteById(String id);
}