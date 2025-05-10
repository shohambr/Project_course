package ServiceLayer;
import DomainLayer.IStoreRepository;
import DomainLayer.Product;
import DomainLayer.Store;
import infrastructureLayer.StoreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreService{
    IStoreRepository StoreRepository;
    ProductService productService;
    private ObjectMapper mapper = new ObjectMapper();

    public StoreService(IStoreRepository StoreRepository, ProductService productService) {
        this.StoreRepository = StoreRepository;
        this.productService = productService;
    }

    // public void addStore(Store store){
    //     numericId++;
    //     id = String.valueOf(numericId);
    //     StoreRepository.addStore(store);
    // }

    // public Store createStore(){
    //     Store store = new Store();
    //     store.setId(id);
    //     int numericId = Integer.parseInt(id);
    //     numericId++;
    //     id = String.valueOf(numericId);
    //     StoreRepository.addStore(store);
    //     return store;
    // }

    // public void removeStore(Store store){
    //     StoreRepository.removeStore(store);
    // }

    // public void setRating(Store store, Double rating){
    //     store.setRating(rating);
    // }

    public String getStoreName(String id) throws Exception {
            return mapper.readValue(StoreRepository.getStore(id), Store.class).getName();
    }

    public Optional<String> getStoreByName(String name) {
        try {
            Map<String, String> stores = StoreRepository.getStores();
            for (String storeString : stores.keySet()) {
                if (mapper.readValue(StoreRepository.getStore(stores.get(storeString)), Store.class).getName().equals(name)) {
                    return Optional.of(storeString);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR finding store by Name:" + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Optional<String> getStoreById(String id) {
        try {
            Map<String, String> stores = StoreRepository.getStores();
            for (String store : stores.keySet()) {
                if (stores.get(store).equals(id)) {
                    return Optional.of(store);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR finding store by id:" + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

}