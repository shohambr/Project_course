package ServiceLayer;
import DomainLayer.IPayment;
import DomainLayer.IShipping;
import DomainLayer.User;

import java.util.*;

public class ShippingService {
    private IShipping shippingSystem;

    public ShippingService(IShipping shippingSystem) {
        this.shippingSystem = shippingSystem;
    }

    public boolean processShipping(User user, String state, String city, String street, String homeNumber) {
        try {
            shippingSystem.processShipping(state, city, street, homeNumber);
            EventLogger.logEvent(user.getID(), "Shipping successful");
            return true;
        } catch (Exception e) {
            System.out.println("Error encountered while processing shipping:" + e.getMessage());
            ErrorLogger.logError(user.getID(), "Error in shipping", e.getMessage());
            return false;
        }
    }
}