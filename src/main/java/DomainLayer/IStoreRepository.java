package DomainLayer;

import java.util.List;
import java.util.Optional;

public interface IStoreRepository {
    void addStore(Store store);
    void removeStore(Store store);
    Store getStore(String id);
    Optional<String> findByName(String name);
    List<Store> findAll();
}
