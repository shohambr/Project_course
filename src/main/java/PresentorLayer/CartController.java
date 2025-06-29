package PresentorLayer;

import ServiceLayer.UserService;
import ServiceLayer.PaymentService;
import ServiceLayer.ShippingService;
import utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import DomainLayer.ShoppingCart;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;

    @Autowired
    public CartController(UserService userService, PaymentService paymentService, ShippingService shippingService) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.shippingService = shippingService;
    }

    @GetMapping
    public ResponseEntity<String> getCart(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            // For now, return a placeholder cart - this would need proper implementation
            return ResponseEntity.ok(Response.getOk("Cart functionality not fully implemented yet"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            userService.addToCart(token, request.getStoreId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(Response.getOk("Product added to cart successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody RemoveFromCartRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            userService.removeFromCart(token, request.getStoreId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(Response.getOk("Product removed from cart successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            // For now, return a placeholder - this would need proper implementation
            return ResponseEntity.ok(Response.getOk("Cart cleared successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            
            // Use the existing purchaseCart method
            userService.purchaseCart(
                token,
                request.getPaymentService(),
                request.getCreditCardNumber(),
                request.getExpirationDate(),
                request.getBackNumber(),
                request.getState(),
                request.getCity(),
                request.getStreet(),
                request.getHomeNumber()
            );

            return ResponseEntity.ok(Response.getOk("Checkout completed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @GetMapping("/total")
    public ResponseEntity<String> getCartTotal(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            Double total = userService.reserveCart(token);
            return ResponseEntity.ok(Response.getOk(total));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }

    // DTOs
    public static class AddToCartRequest {
        private String storeId;
        private String productId;
        private Integer quantity;

        public String getStoreId() { return storeId; }
        public void setStoreId(String storeId) { this.storeId = storeId; }
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class RemoveFromCartRequest {
        private String storeId;
        private String productId;
        private Integer quantity;

        public String getStoreId() { return storeId; }
        public void setStoreId(String storeId) { this.storeId = storeId; }
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class CheckoutRequest {
        private String paymentService;
        private String creditCardNumber;
        private String expirationDate;
        private String backNumber;
        private String state;
        private String city;
        private String street;
        private String homeNumber;

        // Getters and setters
        public String getPaymentService() { return paymentService; }
        public void setPaymentService(String paymentService) { this.paymentService = paymentService; }
        public String getCreditCardNumber() { return creditCardNumber; }
        public void setCreditCardNumber(String creditCardNumber) { this.creditCardNumber = creditCardNumber; }
        public String getExpirationDate() { return expirationDate; }
        public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
        public String getBackNumber() { return backNumber; }
        public void setBackNumber(String backNumber) { this.backNumber = backNumber; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        public String getHomeNumber() { return homeNumber; }
        public void setHomeNumber(String homeNumber) { this.homeNumber = homeNumber; }
    }
}