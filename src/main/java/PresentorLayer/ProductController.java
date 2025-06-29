package PresentorLayer;

import ServiceLayer.UserService;
import ServiceLayer.ProductService;
import ServiceLayer.RegisteredService;
import utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import DomainLayer.Product;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)  
public class ProductController {

    private final UserService userService;
    private final ProductService productService;
    private final RegisteredService registeredService;

    @Autowired
    public ProductController(UserService userService, ProductService productService, RegisteredService registeredService) {
        this.userService = userService;
        this.productService = productService;
        this.registeredService = registeredService;
    }

    @GetMapping
    public ResponseEntity<String> getAllProducts(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            List<Product> products = userService.getAllProducts(token);
            return ResponseEntity.ok(Response.getOk(products));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<String> getProduct(@PathVariable String productId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            Product product = productService.getProductById(productId).orElse(null);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(Response.getOk(product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<String> getProductsByStore(@PathVariable String storeId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            // For now, return all products - this would need to be implemented properly
            List<Product> products = userService.getAllProducts(token);
            return ResponseEntity.ok(Response.getOk(products));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchProducts(@RequestParam String query, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            List<String> productIds = userService.findProduct(token, query, "");
            return ResponseEntity.ok(Response.getOk(productIds));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody AddProductRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            // For now, we'll use addToCart functionality as a placeholder
            userService.addToCart(token, request.getStoreId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(Response.getOk("Product added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping("/{productId}/rate")
    public ResponseEntity<String> rateProduct(@PathVariable String productId, @RequestBody RateRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            boolean success = registeredService.rateProduct(token, productId, request.getRating());
            return ResponseEntity.ok(Response.getOk(success ? "Product rated successfully" : "Failed to rate product"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<String> getProductsByCategory(@PathVariable String category, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            List<String> productIds = userService.findProduct(token, "", category);
            return ResponseEntity.ok(Response.getOk(productIds));
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
    public static class AddProductRequest {
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

    public static class RateRequest {
        private int rating;

        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
    }
}