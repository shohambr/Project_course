package PresentorLayer;

import ServiceLayer.RegisteredService;
import utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private final RegisteredService registeredService;

    @Autowired
    public OrderController(RegisteredService registeredService) {
        this.registeredService = registeredService;
    }

    @GetMapping("/history")
    public ResponseEntity<String> getOrderHistory(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            List<String> orders = registeredService.getUserOrderHistory(token);
            return ResponseEntity.ok(Response.getOk(orders));
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
}