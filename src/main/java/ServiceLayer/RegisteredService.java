package ServiceLayer;

import DomainLayer.IStoreRepository;
import DomainLayer.IOrderRepository;
import DomainLayer.IProductRepository;
import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import DomainLayer.domainServices.History;
import DomainLayer.domainServices.Rate;
import DomainLayer.domainServices.UserConnectivity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisteredService {
    private final IToken tokenService;
    private final IUserRepository userRepo;
    private final IStoreRepository storeRepo;
    private final IProductRepository productRepo;
    private final IOrderRepository orderRepo;
    private final UserConnectivity userConnectivity;
    private final Rate rateService;
    private final History history;

    public RegisteredService(IUserRepository userRepo, IToken tokenService , IStoreRepository storeRepo, IProductRepository productRepo , IOrderRepository orderRepo) {
        this.orderRepo = orderRepo;
        this.storeRepo = storeRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.tokenService = tokenService;
        this.userConnectivity = new UserConnectivity(tokenService, userRepo);
        this.rateService = new Rate(tokenService, storeRepo , userRepo, productRepo);
        this.history = new History(tokenService, orderRepo, userRepo);
    }


    public String logoutRegistered(String token) throws Exception {
        String username = tokenService.extractUsername(token);
        try {
            userConnectivity.logout(username, token);
            EventLogger.logEvent(username, "LOGOUT");
            return tokenService.generateToken("Guest");
        }catch (IllegalArgumentException e) {
            EventLogger.logEvent(username, "LOGOUT_FAILED" );
            throw new RuntimeException("Invalid token");
        }
    }

    public boolean rateStore(String token, String storeId, int rate) throws Exception {
        try {
            EventLogger.logEvent(tokenService.extractUsername(token), "RATE_STORE");
            return rateService.rateStore(token, storeId, rate);
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(tokenService.extractUsername(token), "RATE_STORE_FAILED");
            throw new RuntimeException("Invalid token");
        }
    }

    public boolean rateProduct(String token, String productId, int rate) throws Exception {
        try {
            EventLogger.logEvent(tokenService.extractUsername(token), "RATE_PRODUCT");
            return rateService.rateProduct(token, productId, rate);
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(tokenService.extractUsername(token), "RATE_PRODUCT_FAILED");
            throw new RuntimeException("Invalid token");
        }
    }

    public boolean rateStoreAndProduct(String token, String storeId, String productId, int storeRate, int productRate) throws Exception {
        try {
            EventLogger.logEvent(tokenService.extractUsername(token), "RATE_STORE_AND_PRODUCT");
            boolean storeRated = rateService.rateStore(token, storeId, storeRate);
            boolean productRated = rateService.rateProduct(token, productId, productRate);
            return storeRated && productRated;
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(tokenService.extractUsername(token), "RATE_STORE_AND_PRODUCT_FAILED");
            throw new RuntimeException("Invalid token");
        }
    }


    public List<String> getUserOrderHistory(String token) throws Exception {
        String username = tokenService.extractUsername(token);
        try {
            EventLogger.logEvent(username, "GET_ORDER_HISTORY");
            return history.getOrderHistory(token);
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(username, "GET_ORDER_HISTORY_FAILED");
            throw new RuntimeException("Invalid token");
        }
    }
}
