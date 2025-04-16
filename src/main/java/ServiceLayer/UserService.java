package ServiceLayer;

import DomainLayer.IUserRepository;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.ShoppingCart;

import java.util.List;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserService {

    private final TokenService tokenService;
    private final IUserRepository userRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserService(IUserRepository repository, TokenService tokenService) {
        this.userRepo = repository;
        this.tokenService = tokenService;
    }

    public String login(String username, String password) {
        if (!userRepo.isUserExist(username)) {
            return "username does not exist";
        }

        String hashedPassword = userRepo.getUserPass(username);
        if (BCrypt.checkpw(password, hashedPassword)) {
            String token = tokenService.generateToken(username);
            return token;
        }

        return "incorrect password";
    }

    public String signUp(String username, String password) {
        if (userRepo.isUserExist(username)) {
            return "username already exists";
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        userRepo.addUser(username, hashedPassword);
        return tokenService.generateToken(username);
    }

    public void logoutRegistered(String token, String userValue) throws JsonProcessingException {
        RegisteredUser user = mapper.readValue(userValue, RegisteredUser.class);
        String id = String.valueOf(user.getID());
        userRepo.update(id, userValue);
        tokenService.invalidateToken(token);
    }

    public String purchaseCart(int userId, String token, ShoppingCart cart) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }

        double totalPrice = cart.purchaseCart();

        if (totalPrice <= 0) {
            return "Cart is empty";
        }

        return "Purchase successful. Total paid: $" + totalPrice;
    }

    protected RegisteredUser deserializeUser(String json) throws JsonProcessingException {
        return mapper.readValue(json, RegisteredUser.class);
    }

    public List<String> searchItems(String description , String category) {
        // Dummy implementation
        return new ArrayList<>();
    }

    public List<String> searchItemsInStore(String description , String category , String storeName) {
        // Dummy implementation with sort by rating logic placeholder
        return new ArrayList<>();
    }

    public String createStore(String storeName, int userId, String token) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        // Dummy store creation logic
        return "Store '" + storeName + "' created by user ID: " + userId;
    }

    public String rateItem(String itemName, int rating, String token) {
        if (!tokenService.validateToken(token)) {
            return "Invalid or expired token";
        }
        // Dummy rating logic
        return "Rated item '" + itemName + "' with " + rating + " stars.";
    }

    public String rateStore(String storeName, int rating, String token) {
        if (!tokenService.validateToken(token)) {
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
