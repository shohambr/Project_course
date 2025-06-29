package PresentorLayer;

import ServiceLayer.RegisteredService;
import ServiceLayer.TokenService;
import utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final RegisteredService registeredService;
    private final TokenService tokenService;

    @Autowired
    public UserController(RegisteredService registeredService, TokenService tokenService) {
        this.registeredService = registeredService;
        this.tokenService = tokenService;
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            String username = tokenService.extractUsername(token);
            UserProfile profile = new UserProfile(username);
            return ResponseEntity.ok(Response.getOk(profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.getError(e));
        }
    }

    @PostMapping("/notifications/{storeId}")
    public ResponseEntity<String> sendNotificationToStore(@PathVariable String storeId, @RequestBody NotificationRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            registeredService.sendNotificationToStore(token, storeId, request.getMessage());
            return ResponseEntity.ok(Response.getOk("Notification sent successfully"));
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
    public static class UserProfile {
        private String username;

        public UserProfile(String username) {
            this.username = username;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    public static class NotificationRequest {
        private String message;

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}