package PresentorLayer;

import ServiceLayer.UserService;
import ServiceLayer.RegisteredService;
import ServiceLayer.StoreService;
import utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import DomainLayer.Store;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StoreController {

    private final UserService userService;
    private final RegisteredService registeredService;
    private final StoreService storeService;

    @Autowired
    public StoreController(UserService userService, RegisteredService registeredService, StoreService storeService) {
        this.userService = userService;
        this.registeredService = registeredService;
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<String> getAllStores(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            List<Store> stores = storeService.getAllStores();
            return ResponseEntity.ok(Response.getOk(stores));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<String> getStore(@PathVariable String storeId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            Store store = storeService.getStoreById(storeId);
            if (store == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(Response.getOk(store));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping
    public ResponseEntity<String> createStore(@RequestBody CreateStoreRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            String storeId = registeredService.openStore(token);
            return ResponseEntity.ok(Response.getOk(new StoreResponse(storeId, "Store created successfully")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping("/{storeId}/rate")
    public ResponseEntity<String> rateStore(@PathVariable String storeId, @RequestBody RateRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            boolean success = registeredService.rateStore(token, storeId, request.getRating());
            return ResponseEntity.ok(Response.getOk(success ? "Store rated successfully" : "Failed to rate store"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchStores(@RequestParam String query, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            List<String> storeIds = userService.getStoreByName(token, query);
            List<Store> stores = new ArrayList<>();
            for (String storeId : storeIds) {
                Store store = storeService.getStoreById(storeId);
                if (store != null) {
                    stores.add(store);
                }
            }
            return ResponseEntity.ok(Response.getOk(stores));
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
    public static class CreateStoreRequest {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class RateRequest {
        private int rating;

        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
    }

    public static class StoreResponse {
        private String storeId;
        private String message;

        public StoreResponse(String storeId, String message) {
            this.storeId = storeId;
            this.message = message;
        }

        public String getStoreId() { return storeId; }
        public void setStoreId(String storeId) { this.storeId = storeId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}