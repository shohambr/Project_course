package InfrastructureLayer;
import DomainLayer.IStoreRepository;
import DomainLayer.Product;
import DomainLayer.Store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreRepository implements IStoreRepository {
    // Changed to Map<String, Store> for more efficient lookups by ID
    private Map<String, Store> stores = new HashMap<>();
    private static StoreRepository instance;
    //entry in the hashmap is of the form <StoreID , (pass;json)>
    private Map<String, String> repo = new HashMap<>();
    public StoreRepository() {}

    @Override
    public void addStore(String storeId, String storeJson) {
        if (stores.containsKey(storeId)) {
            throw new IllegalArgumentException("Store already exists");
        }
        repo.put(storeId, storeJson);
    }

    @Override
    public void removeStore(String store) {
        repo.remove(store);
    }

    @Override
    public String getStore(String storeId) {
        return repo.get(storeId);
    }

    @Override
    public void updateStore(String storeId, String storeJson) {
        if (!stores.containsKey(storeId)) {
            return;
        }
        repo.put(storeId, storeJson);
    }
    // Public method to provide access to the singleton instance

    public static synchronized StoreRepository getInstance() {
        if (instance == null) {
            instance = new StoreRepository();
        }
        return instance;
    }

    public void addStore(Store store) {
        // Simplified check since we're using store ID as the key
        if (!stores.containsKey(store.getId())) {
            stores.put(store.getId(), store);
        }
    }

    public void removeStore(Store store) {
        // Simplified removal since we're using store ID as the key
        stores.remove(store.getId());
    }

    public Store getStoreById(String storeId) {
        return stores.get(storeId);
    }

    public List<String> getStoreByName(String storeName) {
        return stores.entrySet().stream()
                .filter(entry -> entry.getValue().contains(storeName))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<String> findAll() {
        return stores.values().stream()
                .map(entry -> entry)
                .toList();
    }

}
