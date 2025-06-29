package ServiceLayer;

import DomainLayer.Store;
import DomainLayer.IStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
public class StoreService {
    
    private final IStoreRepository storeRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public StoreService(IStoreRepository storeRepository) {
        this.storeRepository = storeRepository;
        this.objectMapper = new ObjectMapper();
    }

    public List<Store> getAllStores() {
        try {
            Map<String, String> storeMap = storeRepository.getStores();
            List<Store> stores = new ArrayList<>();
            
            for (String storeJson : storeMap.values()) {
                try {
                    Store store = objectMapper.readValue(storeJson, Store.class);
                    stores.add(store);
                } catch (Exception e) {
                    System.out.println("Error parsing store JSON: " + e.getMessage());
                }
            }
            
            return stores;
        } catch (Exception e) {
            System.out.println("Error getting all stores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Store getStoreById(String storeId) {
        try {
            String storeJson = storeRepository.getStore(storeId);
            if (storeJson != null) {
                return objectMapper.readValue(storeJson, Store.class);
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error getting store by ID: " + e.getMessage());
            return null;
        }
    }

    public void addStore(String storeId, Store store) {
        try {
            String storeJson = objectMapper.writeValueAsString(store);
            storeRepository.addStore(storeId, storeJson);
        } catch (Exception e) {
            System.out.println("Error adding store: " + e.getMessage());
            throw new RuntimeException("Failed to add store");
        }
    }

    public void updateStore(String storeId, Store store) {
        try {
            String storeJson = objectMapper.writeValueAsString(store);
            storeRepository.updateStore(storeId, storeJson);
        } catch (Exception e) {
            System.out.println("Error updating store: " + e.getMessage());
            throw new RuntimeException("Failed to update store");
        }
    }

    public void removeStore(String storeId) {
        try {
            storeRepository.removeStore(storeId);
        } catch (Exception e) {
            System.out.println("Error removing store: " + e.getMessage());
            throw new RuntimeException("Failed to remove store");
        }
    }
}