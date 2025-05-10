package DomainLayer.domainServices;

import DomainLayer.IProductRepository;
import DomainLayer.IStoreRepository;
import DomainLayer.Product;
import DomainLayer.Store;
import ServiceLayer.EventLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search {
    private final IProductRepository productRepository;
    private final IStoreRepository storeRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public Search(IProductRepository productRepository, IStoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    public String searchByName(String partialName) throws JsonProcessingException {
        List<Product> matches = productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(partialName.toLowerCase()))
                .toList();

        EventLogger.logEvent("SEARCH_BY_NAME", "Query=" + partialName + " Matches=" + matches.size());
        return mapper.writeValueAsString(matches);
    }

    public String searchStoreByName(String partialName) throws JsonProcessingException {
        List<String> matches = storeRepository.findAll().stream()
                .filter(p -> {try {
                    if(mapper.readValue(p, Store.class).getName().toLowerCase().contains(partialName.toLowerCase())) {
                        return true;}} catch (Exception e) {} return false;})
                .toList();

        EventLogger.logEvent("SEARCH_STORE_BY_NAME", "Query=" + partialName + " Matches=" + matches.size());
        return mapper.writeValueAsString(matches);
    }

    public String getStoreById(String Id) throws JsonProcessingException {
        Map<String, String> stores = storeRepository.getStores();
        for (String store : stores.keySet()) {
            if(mapper.readValue(stores.get(store), Store.class).getId().equals(Id)) {
                return stores.get(store);
            }
        }
        EventLogger.logEvent("GET_STORE_BY_ID", "Query=" + Id);
        return null;
    }

    public String searchByCategory(String category) throws JsonProcessingException {
        List<Product> matches = productRepository.findAll().stream()
                .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                .toList();

        EventLogger.logEvent("SEARCH_BY_CATEGORY", "Category=" + category + " Matches=" + matches.size());
        return mapper.writeValueAsString(matches);
    }

    public String getProductsByStore(String storeId) throws JsonProcessingException {
        String storeJson = storeRepository.getStore(storeId);
        if (storeJson == null) {
            EventLogger.logEvent("SEARCH_BY_STORE", "Store=" + storeId + " NOT_FOUND");
            throw new IllegalArgumentException("Store not found");
        }

        // ✅ Deserialize JSON to Store object
        ObjectMapper mapper = new ObjectMapper();
        Store store = mapper.readValue(storeJson, Store.class);

        List<Product> result = new ArrayList<>();
        for (String productId : store.getProducts().keySet()) {
            Product product = productRepository.getProduct(productId);
            if (product != null) {
                result.add(product);
            }
        }

        return mapper.writeValueAsString(result);
    }

}
