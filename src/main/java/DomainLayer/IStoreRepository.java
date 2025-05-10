package DomainLayer;

import java.util.Collection;
import java.util.Map;

public interface IStoreRepository {
    void addStore(String storeId , String storeJson);
    void removeStore(String store);
    String getStore(String storeId);
    void updateStore(String storeId, String storeJson);
    Map<String, String> getStores();
    Collection<String> findAll();
}
