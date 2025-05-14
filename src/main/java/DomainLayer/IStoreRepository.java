package DomainLayer;

import java.util.List;

public interface IStoreRepository {
    void addStore(String storeId , String storeJson);
    void removeStore(String store);
    String getStore(String storeId);
    List<String> getStoreByName(String storeName);
    void updateStore(String storeId, String storeJson);
    List<String> findAll();
}
