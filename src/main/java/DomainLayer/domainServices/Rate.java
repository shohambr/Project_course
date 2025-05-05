package DomainLayer.domainServices;

import com.fasterxml.jackson.databind.ObjectMapper;

import DomainLayer.IProductRepository;
import DomainLayer.IStoreRepository;
import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import DomainLayer.Product;
import DomainLayer.Store;

public class Rate {
    private IToken Tokener;
    private ObjectMapper mapper = new ObjectMapper();
    private IStoreRepository storeRepository;
    private IUserRepository userRepository;
    private IProductRepository productRepository;

    public Rate(IToken Tokener, IStoreRepository storeRepository, IUserRepository userRepository, IProductRepository productRepository) {
        this.Tokener = Tokener;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public boolean rateStore(String token, String storeId, int rate) {
        if (token == null || storeId == null || rate < 1 || rate > 5) {
            throw new IllegalArgumentException("Invalid input");
        }
        Tokener.validateToken(token);
        String username = Tokener.extractUsername(token);
        Store store = storeRepository.getStore(storeId);
        if (store == null) {
            throw new IllegalArgumentException("Store does not exist");
        }
        if (userRepository.getUser(username) == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        return store.rate(rate);
    }

    public boolean rateProduct(String token, String productId, double rate) {
        if (token == null || productId == null || rate < 1 || rate > 5) {
            throw new IllegalArgumentException("Invalid input");
        }
        Tokener.validateToken(token);
        String username = Tokener.extractUsername(token);
        Product product = productRepository.getProduct(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product does not exist");
        }
        if (userRepository.getUser(username) == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        return product.addRating( username, rate);
    }
}
