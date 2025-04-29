package ServiceLayer;
import DomainLayer.IPayment;
import DomainLayer.IShipping;
import DomainLayer.Store;
import DomainLayer.User;
import DomainLayer.domainServices.ShippingConnectivity;

import java.util.*;

public class ShippingService {
    private ShippingConnectivity shippingConnectivity;

    public ShippingService(IShipping ProxyShipping) {
        this.shippingConnectivity = new ShippingConnectivity(ProxyShipping);
    }

    public boolean processShipping(User user, Store store, String state, String city, String street, String homeNumber) {
        try {
            shippingConnectivity.processShipping(user, store, state, city, street, homeNumber);
            EventLogger.logEvent(user.getID(), "Shipping successful");
            return true;
        } catch (Exception e) {
            System.out.println("Error encountered while processing shipping:" + e.getMessage());
            ErrorLogger.logError(user.getID(), "Error in shipping", e.getMessage());
            return false;
        }
    }
}