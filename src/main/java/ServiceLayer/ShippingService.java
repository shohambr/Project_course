package ServiceLayer;
import DomainLayer.IPayment;
import DomainLayer.IShipping;

import java.util.*;

public class ShippingService {
    private IShipping shippingSystem;

    public ShippingService(IShipping shippingSystem) {
        this.shippingSystem = shippingSystem;
    }

    public boolean processShipping(String state, String city, String street, String homeNumber) {
        try {
            shippingSystem.processShipping(state, city, street, homeNumber);
            return true;
        } catch (Exception e) {
            System.out.println("Error encountered while processing shipping:" + e.getMessage());
            return false;
        }
    }

}