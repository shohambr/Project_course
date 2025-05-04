package ServiceLayer;

import DomainLayer.*;
import DomainLayer.Roles.Guest;
import DomainLayer.Roles.Jobs.Job;
import DomainLayer.domainServices.UserCart;
import DomainLayer.domainServices.UserConnectivity;
import DomainLayer.Roles.RegisteredUser;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Optional;

import org.springframework.stereotype.Service;
import utils.ProductKeyModule;

import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final IToken tokenService;
    private final IUserRepository userRepo;
    private final ObjectMapper mapper = new ObjectMapper();
    private final JobService jobService;
    private final ProductService productService;
    private final UserConnectivity userConnectivity;
    private final UserCart userCart;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final StoreService storeService;


    public UserService(IUserRepository repository, IToken tokenService, JobService jobService, ProductService productService, PaymentService paymentService, ShippingService shippingService, StoreService storeService) {
        this.productService = productService;
        this.userRepo = repository;
        this.tokenService = tokenService;
        this.userConnectivity = new UserConnectivity(tokenService);
        this.jobService = jobService;
        this.mapper.registerModule(new ProductKeyModule());
        this.mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.userCart = new UserCart(tokenService);
        this.paymentService = paymentService;
        this.shippingService = shippingService;
        this.storeService = storeService;
    }


    public RegisteredUser login(String username, String password) throws JsonProcessingException {
        try {
            userConnectivity.login(username, password , userRepo.getUserPass(username));
            EventLogger.logEvent(username , "LOGIN");
            RegisteredUser user = mapper.readValue(userRepo.getUser(username), RegisteredUser.class);
            user.setToken(tokenService.generateToken(username));
            return user;
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(username, "LOGIN_FAILED");
            throw new RuntimeException("Invalid username or password");
        }
    }

    public RegisteredUser signUp(String username, String password)  throws Exception {
        try {
            userConnectivity.signUp(username, password);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            RegisteredUser user = new RegisteredUser(new ArrayList<>(), username);
            userRepo.addUser(username, hashedPassword, mapper.writeValueAsString(user));
            user.setToken(tokenService.generateToken(username));
            EventLogger.logEvent(username , "SIGNUP");
            return user;
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(username, "SIGNUP_FAILED");
            throw new RuntimeException("User already exists");
        }
    }

    public Guest logoutRegistered(String token, RegisteredUser user) throws Exception {
        try {
            userConnectivity.logout(user.getName(), token);
            userRepo.update(user.getName(), mapper.writeValueAsString(user));
            EventLogger.logEvent(user.getName(), "LOGOUT");
            return new Guest();
        }catch (IllegalArgumentException e) {
            EventLogger.logEvent(user.getName(), "LOGOUT_FAILED");
            throw new RuntimeException("Invalid token");
        }
    }


    public String removeFromCart(String token, RegisteredUser u,Store store , Product product) {
        try{
            userCart.removeFromCart(token, u, store , product);
            EventLogger.logEvent(u.getName(), "REMOVE_FROM_CART");
            return "Product removed from cart";
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(u.getName(), "REMOVE_FROM_CART_FAILED");
            throw new RuntimeException("Failed to remove product from cart");
        }
    }

    public String addToCart(String token, RegisteredUser u, Store store , Product product) {
        try{
            userCart.addToCart(token, u, store , product);
            EventLogger.logEvent(u.getName(), "ADD_TO_CART");
            return "Product added to cart";
        } catch (IllegalArgumentException e) {
            EventLogger.logEvent(u.getName(), "ADD_TO_CART_FAILED");
            throw new RuntimeException("Failed to add product to cart");
        }
    }

     @Transactional
     public String purchaseCart(User user, String token, String creditCardNumber, String expirationDate, String backNumber, String paymentService, String state, String city, String street, String homeNumber) {
         try {
             if(!this.paymentService.processPayment(user, creditCardNumber, expirationDate, backNumber, paymentService)) {
                 throw new Exception("Payment service failed");
             }
             if(this.shippingService.processShipping(user, state, city, street, homeNumber)) {
                 throw new Exception("Shipping service failed");
             }
             userCart.purchaseCart(user, token);
             EventLogger.logEvent(user.getID(), "PURCHASED_CART");
             return "Purchased cart";
         } catch (Exception e) {
             EventLogger.logEvent(user.getID(), "PURCHASE_CART_FAILED" + e.getMessage());
             throw new RuntimeException("Failed to add product to cart");
         }
     }

    public List<String> searchItems(String name , String token) throws Exception {
        //if (!tokenService.validateToken(token)) {
        //    throw new RuntimeException("Invalid or expired token");
        //}
        if (name == null || name.isEmpty()) {
            return Collections.emptyList();
        }

        if (name.equals("all")) {
            return productService.getAllProducts().stream()
                    .map(product -> {
                        try {
                            return mapper.writeValueAsString(product);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to serialize product to JSON", e);
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            return productService.getProductByName(name).stream()
                    .map(product -> {
                        try {
                            return mapper.writeValueAsString(product);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to serialize product to JSON", e);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    public List<String> searchStores(String name , String token) throws Exception {
        //if (!tokenService.validateToken(token)) {
        //    throw new RuntimeException("Invalid or expired token");
        //}
        if (name == null || name.isEmpty()) {
            return Collections.emptyList();
        }

        if (name.equals("all")) {
            return storeService.getAllStores().stream()
                    .map(store -> {
                        try {
                            return mapper.writeValueAsString(store);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to serialize product to JSON", e);
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            return storeService.getStoreByName(name).stream()
                    .map(store -> {
                        try {
                            return mapper.writeValueAsString(store);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to serialize product to JSON", e);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }



//     public List<String> searchItems(String name , String token) throws Exception {
//         //if (!tokenService.validateToken(token)) {
//         //    throw new RuntimeException("Invalid or expired token");
//         //}
//         if (name == null || name.isEmpty()) {
//             return Collections.emptyList();
//         }
//
//         if (name.equals("all")) {
//             return productService.getAllProducts().stream()
//                     .map(product -> {
//                         try {
//                             return mapper.writeValueAsString(product);
//                         } catch (JsonProcessingException e) {
//                             throw new RuntimeException("Failed to serialize product to JSON", e);
//                         }
//                     })
//                     .collect(Collectors.toList());
//         } else {
//             return productService.getProductByName(name).stream()
//                     .map(product -> {
//                         try {
//                             return mapper.writeValueAsString(product);
//                         } catch (JsonProcessingException e) {
//                             throw new RuntimeException("Failed to serialize product to JSON", e);
//                         }
//                     })
//                     .collect(Collectors.toList());
//         }
//     }
//
//    public List<String> searchStores(String name , String token) throws Exception {
//        //if (!tokenService.validateToken(token)) {
//        //    throw new RuntimeException("Invalid or expired token");
//        //}
//        if (name == null || name.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        if (name.equals("all")) {
//            return storeService.getAllStores().stream()
//                    .map(store -> {
//                        try {
//                            return mapper.writeValueAsString(store);
//                        } catch (JsonProcessingException e) {
//                            throw new RuntimeException("Failed to serialize product to JSON", e);
//                        }
//                    })
//                    .collect(Collectors.toList());
//        } else {
//            return storeService.getStoreByName(name).stream()
//                    .map(p -> {
//                        try {
//                            return mapper.writeValueAsString(store);
//                        } catch (JsonProcessingException e) {
//                            throw new RuntimeException("Failed to serialize product to JSON", e);
//                        }
//                    })
//                    .collect(Collectors.toList());
//        }
//    }


//     public List<String> searchItemsInStore(String name , String storeId , String token) throws Exception {
//         if (!tokenService.validateToken(token)) {
//             throw new RuntimeException("Invalid or expired token");
//         }
//         if (name == null || name.isEmpty()) {
//             return Collections.emptyList();
//         }
//         return searchItems(name , token).stream()
//                 .filter(item -> {
//                     try {
//                         Product product = mapper.readValue(item, Product.class);
//                         return product.getStoreId().equals(storeId);
//                     } catch (JsonProcessingException e) {
//                         throw new RuntimeException("Failed to deserialize product from JSON", e);
//                     }
//                 })
//                 .collect(Collectors.toList());
//     }

//     public String getHistory(int userId, String token) {
//         if (!tokenService.validateToken(token)) {
//             return "Invalid or expired token";
//         }
//         // Dummy history retrieval
//         return "History retrieved for user ID: " + userId;
//     }

//     public String bid(String itemName, int price, String token) {
//         if (!tokenService.validateToken(token)) {
//             return "Invalid or expired token";
//         }
//         // Dummy bidding logic
//         return "Bid of $" + price + " placed on item '" + itemName + "'.";
//     }

//     public String purchaseViaBid(String itemName, int price, String token) {
//         if (!tokenService.validateToken(token)) {
//             return "Invalid or expired token";
//         }
//         // Dummy purchase via bid logic
//         return "Purchased item '" + itemName + "' via bid for $" + price + ".";
//     }
//     public String createStore(RegisteredUser user, String token) {
//         if (!tokenService.validateToken(token)) {
//             return "Invalid or expired token";
//         }
//         this.jobService.createStore(user);
//         return null;
//     }
//     public void sendAppointNewOwnerRequest(String token ,Store store, RegisteredUser oldOwner, RegisteredUser newOwner){
//         boolean accepted=false;
//         if(!this.jobService.UserIsOwnerOfStore(store.getId(), newOwner.getID())){
//             StringBuilder requestText = new StringBuilder();
//             requestText.append("hi, ").append(newOwner.getName()).append(".\n").append("I would like for you to be an owner in my store: ").append(store.getId());
//             accepted = newOwner.receivedOwnershipRequest(requestText.toString());
//         }
//         //insert listener for the users answer
//         if (accepted){
//             jobService.addNewOwnerToStore(store,oldOwner,newOwner);
//         }
//     }
//     public void sendAppointNewManagerRequest(Store store, RegisteredUser oldOwner, RegisteredUser newManager, boolean[] permissions){
//         boolean accepted=false;
//         if(!this.jobService.UserIsManagerOfStore(store.getId(), newManager.getID())){
//             StringBuilder requestText = new StringBuilder();
//             requestText.append("hi, ").append(newManager.getName()).append(".\n").append("I would like for you to be a manager at my store: ").append(store.getId());
//             accepted = newManager.receivedManagingRequest(requestText.toString());
//         }
//         //insert listener for the users answer
//         if (accepted){
//             jobService.addNewManagerToStore(store,oldOwner,newManager,permissions);
//         }
//     }

//     public void fireFromMyStore(Store store, RegisteredUser superior, RegisteredUser subordinate) {
//         jobService.fireFromMyStore(store,superior,subordinate);
//     }
//     public void changeManagerPermissions(Store store, RegisteredUser owner, RegisteredUser Manager,boolean[] permissions){
//         jobService.changeManagerPermissions(store, owner, Manager, permissions);
//     }
//     public void closeStore(Store store, RegisteredUser founder){
//         jobService.closeStore(store,founder);
//     }
//     public void openStore(Store store, RegisteredUser founder){
//         jobService.openStore(store,founder);
//     }
//     public String getInfoJobsInStore(Store store, RegisteredUser owner){
//         return jobService.getInfoJobsInStore(store,owner);
//     }
//     public String getInfoOrdersInStore(Store store, RegisteredUser owner){
//         return jobService.getInfoOrdersInStore(store,owner);
//     }
//     public void respondToBuyer(Store store, RegisteredUser owner, RegisteredUser customer,String query){
//         customer.acceptQueryResponse(jobService.respondToBuyer(store,owner,query));
//     }

//     // public String addToCart(String token,RegisteredUser u , Product product, int quantity) {
//     //     if (!tokenService.validateToken(token)) {
//     //         throw new RuntimeException("Invalid or expired token");
//     //     }
//     //     if (product == null) {
//     //         return "Product not found";
//     //     }
//     //     if (quantity <= 0) {
//     //         return "Invalid quantity";
//     //     }
//     //     if (u == null) {
//     //         return "User not found";
//     //     }
//     //     u.getShoppingCart().addProduct(product, quantity);
//     //     return "Product added to cart";
//     // }

//     public String viewShoppingCart(String token, RegisteredUser u) {
//         if (!tokenService.validateToken(token)) {
//             throw new RuntimeException("Invalid or expired token");
//         }
//         if (u == null) {
//             return "User not found";
//         }
//         return u.getShoppingCart().toString();
//     }

    
}
