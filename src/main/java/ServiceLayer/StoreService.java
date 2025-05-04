package ServiceLayer;
import DomainLayer.IStoreRepository;
import DomainLayer.Product;
import DomainLayer.Store;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import utils.ProductKeyModule;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoreService{
    IStoreRepository StoreRepository;
    ProductService productService;
    private String id = "1";
    private final ObjectMapper mapper = new ObjectMapper();


    public StoreService(IStoreRepository StoreRepository, ProductService productService) {
        this.StoreRepository = StoreRepository;
        this.productService = productService;
        this.mapper.registerModule(new ProductKeyModule());
        this.mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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

    public void setRating(Store store, int rating){
        store.setRating(rating);
    }

    public Store getStoreById(String id) {return StoreRepository.getStore(id);}

    public Optional<String> getStoreByName(String name) {
        try {
            return StoreRepository.findByName(name);
        } catch (Exception e) {
            System.out.println("ERROR finding product by Name:" + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Store> getAllStores() {
        try {
            return StoreRepository.findAll();
        } catch (Exception e) {
            System.out.println("ERROR getting all products: " + e.getMessage());
            return new ArrayList<>();
        }
    }



}