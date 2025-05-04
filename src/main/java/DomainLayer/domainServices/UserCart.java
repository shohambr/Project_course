package DomainLayer.domainServices;
import ServiceLayer.EventLogger;
import DomainLayer.IOrderRepository;
import DomainLayer.IPayment;
import DomainLayer.IProductRepository;
import DomainLayer.IStoreRepository;
import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import DomainLayer.Product;
import DomainLayer.ShoppingBag;
import DomainLayer.ShoppingCart;
import DomainLayer.Store;
import DomainLayer.Order;
import DomainLayer.Roles.RegisteredUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonProcessingException;

public class UserCart {
    private IToken Tokener;
    private ObjectMapper mapper = new ObjectMapper();
    private IStoreRepository storeRepository;
    private IUserRepository userRepository;
    private IProductRepository productRepository;
    private IOrderRepository orderRepository;
    private IPayment paymentSystem;

    public UserCart(IToken Tokener , IUserRepository userRepository, IStoreRepository storeRepository , IProductRepository productRepository , IPayment paymentSystem , IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.Tokener = Tokener;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.paymentSystem = paymentSystem;
    }
    
    public void removeFromCart(String token , String storeId , String productId , Integer quantity) throws JsonProcessingException {
        if (token == null ){
            EventLogger.logEvent(Tokener.extractUsername(token), "REMOVE_FROM_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token cannot be null");
        }
        if (storeId == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "REMOVE_FROM_CART_FAILED - NULL");
            throw new IllegalArgumentException("StoreId cannot be null");
        }
        if (productId == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "REMOVE_FROM_CART_FAILED - NULL");
            throw new IllegalArgumentException("ProductId cannot be null");
        }
        if (quantity == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "REMOVE_FROM_CART_FAILED - NULL");
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (quantity <= 0) {
            EventLogger.logEvent(Tokener.extractUsername(token), "REMOVE_FROM_CART_FAILED - INVALID_QUANTITY");
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        String username = Tokener.extractUsername(token);
        String userJson = userRepository.getUser(username);
        if (userJson == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "REMOVE_FROM_CART_FAILED - USER_NOT_FOUND");
            throw new IllegalArgumentException("User not found");
        }
        RegisteredUser user = mapper.readValue(userJson, RegisteredUser.class);
        Tokener.validateToken(token);
        user.removeProduct(storeId, productId , quantity);
        userRepository.update(username, mapper.writeValueAsString(user));
        EventLogger.logEvent(user.getUsername(), "REMOVE_FROM_CART_SUCCESS");
    }

    public void addToCart(String token ,String storeId , String productId , Integer quantity) throws JsonProcessingException {
        if (token == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token cannot be null");
        }
        if (storeId == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("StoreId cannot be null");
        }
        if (productId == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("ProductId cannot be null");
        }
        if (quantity == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - NULL");
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (quantity <= 0) {
            EventLogger.logEvent(Tokener.extractUsername(token), "ADD_TO_CART_FAILED - INVALID_QUANTITY");
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        String username = Tokener.extractUsername(token);
        RegisteredUser user = mapper.readValue(userRepository.getUser(username), RegisteredUser.class);
        Tokener.validateToken(token);
        user.addProduct(storeId, productId , quantity);
        userRepository.update(username, mapper.writeValueAsString(user));
        EventLogger.logEvent(user.getUsername(), "ADD_TO_CART_SUCCESS");
    }

    public Double reserveCart(String token) throws JsonProcessingException {
        if (token == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "PURCHASE_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token cannot be null");
        }
        String username = Tokener.extractUsername(token);
        Tokener.validateToken(token);
        double totalPrice = 0;
        RegisteredUser user = mapper.readValue(userRepository.getUser(username), RegisteredUser.class);
        ShoppingCart cart = user.getShoppingCart();
        Map<String, Integer> reservedProducts = new HashMap<>();
        for (ShoppingBag bag : cart.getShoppingBags()) {
            String storeId = bag.getStoreId();
            Store store = mapper.convertValue(storeRepository.getStore(storeId), Store.class);
            if (store == null) {
                EventLogger.logEvent(user.getUsername(), "RESERVE_CART_FAILED - STORE_NOT_FOUND");
                throw new IllegalArgumentException("Store not found");
            }
            for (Map.Entry<String, Integer> entry : bag.getProducts().entrySet()) {
                String productId = entry.getKey();
                Integer quantity = entry.getValue();
                Product product = productRepository.getProduct(productId);
                if (product == null) {
                    EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - PRODUCT_NOT_FOUND");
                    throw new IllegalArgumentException("Product not found");
                }
                if (product.getQuantity() < quantity) {
                    throw new IllegalArgumentException("Insufficient stock for product: " + productId);
                }
                if(store.reserveProduct(productId, quantity)){
                    EventLogger.logEvent(user.getUsername(), "RESERVE_PRODUCT_SUCCESS");
                    reservedProducts.put(productId, quantity);
                }else{
                    unreserveCart(reservedProducts, user.getUsername());
                    EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - RESERVE_FAILED");
                    throw new IllegalArgumentException("Failed to reserve product: " + productId);
                }
                totalPrice += product.getPrice() * quantity;
            }
        }

        // if (totalPrice <= 0) {
        //     throw new IllegalArgumentException("Total price must be greater than 0");
        // }
        user.setCartReserved(true);
        userRepository.update(username, mapper.writeValueAsString(user));
        return totalPrice;
    }
    
    public void unreserveCart(Map<String, Integer> reservedProducts ,String username) throws JsonProcessingException {
        for(Map.Entry<String, Integer> entry : reservedProducts.entrySet()) {
            String productId = entry.getKey();
            Integer quantity = entry.getValue();
            Product product = productRepository.getProduct(productId);
            if (product == null) {
                EventLogger.logEvent(username, "UNRESERVE_CART_FAILED - PRODUCT_NOT_FOUND");
                throw new IllegalArgumentException("Product not found");
            }
            if (product.getQuantity() < quantity) {
                EventLogger.logEvent(username, "UNRESERVE_CART_FAILED - INSUFFICIENT_STOCK");
                throw new IllegalArgumentException("Insufficient stock for product: " + productId);
            }
            Store store = mapper.convertValue(storeRepository.getStore(product.getStoreId()), Store.class);
            if (store == null) {
                EventLogger.logEvent(username, "UNRESERVE_CART_FAILED - STORE_NOT_FOUND");
                throw new IllegalArgumentException("Store not found");
            }
            store.unreserveProduct(productId, quantity);
        }
    }

    public void purchaseCart(String token , Double totalPrice ,  String creditCardNumber, String expirationDate, String backNumber) throws Exception {
        if (token == null) {
            EventLogger.logEvent(Tokener.extractUsername(token), "PURCHASE_CART_FAILED - NULL");
            throw new IllegalArgumentException("Token cannot be null");
        }
        String username = Tokener.extractUsername(token);
        Tokener.validateToken(token);
        RegisteredUser user = mapper.readValue(userRepository.getUser(username), RegisteredUser.class);
        if (!user.getCartReserved()) {
            EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - CART_NOT_RESERVED");
            throw new IllegalArgumentException("Cart is not reserved");
        }
        paymentSystem.processPayment(totalPrice , creditCardNumber, expirationDate, backNumber);
        ShoppingCart cart = user.getShoppingCart();
        // tell the store the products are sold
        for (ShoppingBag bag : cart.getShoppingBags()) {
            String storeId = bag.getStoreId();
            Store store = mapper.convertValue(storeRepository.getStore(storeId), Store.class);
            if (store == null) {
                EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - STORE_NOT_FOUND");
                throw new IllegalArgumentException("Store not found");
            }
            for (Map.Entry<String, Integer> entry : bag.getProducts().entrySet()) {
                String productId = entry.getKey();
                Integer quantity = entry.getValue();
                Product product = productRepository.getProduct(productId);
                if (product == null) {
                    EventLogger.logEvent(user.getUsername(), "PURCHASE_CART_FAILED - PRODUCT_NOT_FOUND");
                    throw new IllegalArgumentException("Product not found");
                }
                store.sellProduct(productId, quantity);
            }
        }
        // create an order
        orderRepository.addOrder(new Order(mapper.writeValueAsString(cart), username , totalPrice));
        user.setCartReserved(false);
        user.getShoppingCart().getShoppingBags().clear();
        userRepository.update(username, mapper.writeValueAsString(user));
    }
        
}   //I AM HERE! I ADD IT
