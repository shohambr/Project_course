package ServiceLayer;
import DomainLayer.IStoreRepository;
import DomainLayer.Product;
import DomainLayer.Store;
import infrastructureLayer.StoreRepository;

import java.util.*;

public class StoreService{
    IStoreRepository StoreRepository;
    ProductService productService;
    private String id = "1";


    public StoreService(IStoreRepository StoreRepository, ProductService productService) {
        this.StoreRepository = StoreRepository;
        this.productService = productService;
    }

    public void addStore(Store store){
        store.setId(id);
        int numericId = Integer.parseInt(id);
        numericId++;
        id = String.valueOf(numericId);
        StoreRepository.addStore(store);
    }

    public Store createStore(){
        Store store = new Store();
        store.setId(id);
        int numericId = Integer.parseInt(id);
        numericId++;
        id = String.valueOf(numericId);
        StoreRepository.addStore(store);
        return store;
    }

    public void removeStore(Store store){
        StoreRepository.removeStore(store);
    }

    public void setRating(Store store, Double rating){
        store.setRating(rating);
    }

    public String getStoreName(String id) {return StoreRepository.getStore(id).getName();}

    public Optional<Store> getStoreByName(String name) {
        try {
            Map<Store, String> stores = StoreRepository.getStores();
            for (Store store : stores.keySet()) {
                if (store.getName().equals(name)) {
                    return Optional.of(store);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR finding store by Name:" + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Optional<Store> getStoreById(String id) {
        try {
            Map<Store, String> stores = StoreRepository.getStores();
            for (Store store : stores.keySet()) {
                if (store.getId().equals(id)) {
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