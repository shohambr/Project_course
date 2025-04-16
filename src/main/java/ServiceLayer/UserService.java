package ServiceLayer;

import DomainLayer.IUserRepository;
import DomainLayer.Roles.Guest;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.ShoppingCart;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import DomainLayer.Store;
import DomainLayer.User;
import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserService {

    private final TokenService tokenService;
    private final IUserRepository userRepo;
    private final ObjectMapper mapper = new ObjectMapper();
    private final StoreService storeService;

    public UserService(IUserRepository repository, TokenService tokenService , StoreService storeService) {
        this.userRepo = repository;
        this.tokenService = tokenService;
        this.storeService = storeService;
    }

    public RegisteredUser login(String username, String password) throws JsonProcessingException {
        if (!userRepo.isUserExist(username)) {
            return null;
        }

        String hashedPassword = userRepo.getUserPass(username);
        if (BCrypt.checkpw(password, hashedPassword)) {
            String token = tokenService.generateToken(username);
            String userJson = userRepo.getUser(username);
            RegisteredUser user = deserializeUser(userJson);
            user.setToken(token);
            return user;
        }

        return null;
    }

    public RegisteredUser signUp(String username, String password)  throws JsonProcessingException {
        if (userRepo.isUserExist(username)) {
            return null;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        userRepo.addUser(username, hashedPassword);
        String token = tokenService.generateToken(username);
        RegisteredUser user = new RegisteredUser(new LinkedList<>() , username);
        return user;
    }

    public Guest logoutRegistered(String token, RegisteredUser user) throws Exception {
        if(!tokenService.validateToken(token)){
            throw new Exception("user not logged in");
        }
        String id = String.valueOf(user.getName());
        userRepo.update(id, mapper.writeValueAsString(user));
        tokenService.invalidateToken(token);
        return new Guest();
    }

    public String purchaseCart(int userId, String token, ShoppingCart cart) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }

        double totalPrice = cart.calculatePurchaseCart();

        if (totalPrice <= 0) {
            return "Cart is empty";
        }

        return "Purchase successful. Total paid: $" + totalPrice;
    }

    protected RegisteredUser deserializeUser(String json) throws JsonProcessingException {
        return mapper.readValue(json, RegisteredUser.class);
    }

    public List<String> searchItems(String description , String category) {//should be in product service?
        // Dummy implementation
        return new ArrayList<>();
    }

    public List<String> searchItemsInStore(String description , String category , String storeName) {//should be in product service?
        // Dummy implementation with sort by rating logic placeholder
        return new ArrayList<>();
    }

    public String createStore(RegisteredUser user, String token) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        Store created = this.storeService.createStore();
        user.createStore(created.getId());
        return null;
    }

    public String rateItem(String itemName, int rating, String token) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        // Dummy rating logic
        return "Rated item '" + itemName + "' with " + rating + " stars.";
    }

    public boolean becomeNewOwnerRequest(String messageFromTheOwner) {
        //to implement
        return false;
    }

    public boolean becomeNewManagerRequest(String messageFromTheOwner) {
        //to implement
        return false;
    }

    public String rateStore(String storeName, int rating, String token) {
        if (!this.tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        // Dummy rating logic
        return "Rated store '" + storeName + "' with " + rating + " stars.";
    }

    public String sendMessage(String message, int userId, String token) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        // Dummy messaging logic
        return "Message sent from user ID: " + userId;
    }

    public String getHistory(int userId, String token) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        // Dummy history retrieval
        return "History retrieved for user ID: " + userId;
    }

    public String bid(String itemName, int price, String token) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        // Dummy bidding logic
        return "Bid of $" + price + " placed on item '" + itemName + "'.";
    }

    public String purchaseViaBid(String itemName, int price, String token) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        // Dummy purchase via bid logic
        return "Purchased item '" + itemName + "' via bid for $" + price + ".";
    }
}
