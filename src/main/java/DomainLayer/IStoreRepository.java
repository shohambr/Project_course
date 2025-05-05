package DomainLayer;

import java.util.Map;
import java.util.Optional;

public interface IStoreRepository {
    void addStore(Store store);
    void removeStore(Store store);
    Store getStore(String storeId);
    void updateStore(String storeId, String storeJson);
    Map<Store, String> getStores();
}
