package InfrastructureLayer;
import DomainLayer.IStoreRepository;
import DomainLayer.Product;
import DomainLayer.Store;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StoreRepository implements IStoreRepository {
    private Map<String, String> stores = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public void addStore(String storeId , String storeJson) {
        stores.put(storeId, storeJson);
    }


    public void removeStore(String storeId) {
        stores.remove(storeId);
    }


    public String getStore(String storeId) {
        return stores.get(storeId);
    }

    public boolean existsById(String storeId) {
        return stores.containsKey(storeId);
    }

    public Store getById(String storeId) {
        String json = stores.get(storeId);
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, Store.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void update(Store store) {
        try {
            String json = mapper.writeValueAsString(store);
            stores.put(store.getId(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to update store", e);
        }
    }


    public void updateStore(String storeId, String storeJson) {
        if(stores.containsKey(storeId)) {
            stores.put(storeId, storeJson);
        } else {
            throw new IllegalArgumentException("Store does not exist");
        }
    }

    public Map<String, String> getStores() {
        return stores;
    }
    public boolean getIsStoreDeleted(String storeId) {
        return !stores.containsKey(storeId);
    }

    public void close() {
        stores.clear();
    }

    public List<String> findAll() {return stores.values().stream().toList();}



}