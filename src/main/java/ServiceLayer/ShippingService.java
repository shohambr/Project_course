package ServiceLayer;
import DomainLayer.IPayment;
import DomainLayer.IShipping;
import DomainLayer.Store;
import DomainLayer.User;
import DomainLayer.domainServices.ShippingConnectivity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShippingService {
    private ShippingConnectivity shippingConnectivity;

    public ShippingService(IShipping ProxyShipping) {
        this.shippingConnectivity = new ShippingConnectivity(ProxyShipping);
    }

    public boolean processShipping(User user, String state, String city, String street, String homeNumber) {
        try {
            shippingConnectivity.processShipping(user, state, city, street, homeNumber);
            EventLogger.logEvent(user.getID(), "Shipping successful");
            return true;
        } catch (Exception e) {
            System.out.println("Error encountered while processing shipping:" + e.getMessage());
            ErrorLogger.logError(user.getID(), "Error in shipping", e.getMessage());
            return false;
        }
    }

}