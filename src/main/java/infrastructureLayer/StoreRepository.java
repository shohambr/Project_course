package infrastructureLayer;
import DomainLayer.IStoreRepository;
import DomainLayer.Product;
import DomainLayer.Store;

import java.util.HashMap;
import java.util.Map;

public class StoreRepository implements IStoreRepository {
    private Map<String, String> stores = new HashMap<>();

    public void addStore(String storeId , String storeJson) {
        stores.put(storeId, storeJson);
    }


    public void removeStore(String storeId) {
        stores.remove(storeId);
    }


    public String getStore(String storeId) {
        return stores.get(storeId);
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

}
