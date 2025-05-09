package ServiceLayer;

import DomainLayer.IStoreRepository;
import DomainLayer.INotificationRepository;
import DomainLayer.IOrderRepository;
import DomainLayer.IProductRepository;
import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import DomainLayer.DomainServices.History;
import DomainLayer.DomainServices.toNotify;
import DomainLayer.DomainServices.OpenStore;
import DomainLayer.DomainServices.Rate;
import DomainLayer.DomainServices.UserConnectivity;
import DomainLayer.DomainServices.History;
import DomainLayer.DomainServices.Rate;
import DomainLayer.DomainServices.UserConnectivity;

import java.util.List;

public class RegisteredService {
    private final IToken tokenService;
    private final IUserRepository userRepo;
    private final IStoreRepository storeRepo;
    private final IProductRepository productRepo;
    private final IOrderRepository orderRepo;
    private final INotificationRepository notificationRepo;
    private final UserConnectivity userConnectivity;
    private final Rate rateService;
    private final History history;
    private final OpenStore opener;
    private final toNotify notifyService;

    public RegisteredService(IUserRepository userRepo, IToken tokenService , IStoreRepository storeRepo, IProductRepository productRepo , IOrderRepository orderRepo , INotificationRepository notificationRepo) {
        this.notificationRepo = notificationRepo;
        this.orderRepo = orderRepo;
        this.storeRepo = storeRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.tokenService = tokenService;
        this.userConnectivity = new UserConnectivity(tokenService, userRepo);
        this.rateService = new Rate(tokenService, storeRepo , userRepo, productRepo);
        this.history = new History(tokenService, orderRepo, userRepo);
        this.opener = new OpenStore(tokenService, storeRepo, userRepo);
        this.notifyService = new toNotify(notificationRepo, tokenService);
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

    public String openStore(String token) throws Exception {
        String username = tokenService.extractUsername(token);
        try {
            EventLogger.logEvent(username, "OPEN_STORE");
            return opener.openStore(token);
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(username, "OPEN_STORE_FAILED");
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


    public void sendNotificationToStore(String token, String storeId, String message) throws Exception {
        String username = tokenService.extractUsername(token);
        try {
            EventLogger.logEvent(username, "SEND_NOTIFICATION_TO_STORE");
            notifyService.sendNotificationToStore(token, storeId, message);
        } catch (Exception e) {
            EventLogger.logEvent(username, "SEND_NOTIFICATION_TO_STORE_FAILED " + e.getMessage());
            throw new RuntimeException("Invalid token");
        }
    }
}
