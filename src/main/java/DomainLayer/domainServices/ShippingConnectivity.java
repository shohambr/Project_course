package DomainLayer.domainServices;

import DomainLayer.IShipping;
import DomainLayer.Store;
import DomainLayer.User;

public class ShippingConnectivity {

    private IShipping proxyShipping;

    public ShippingConnectivity(IShipping proxyShipping) {
        this.proxyShipping = proxyShipping;
    }

    public void processShipping(User user, Store store, String state, String city, String street, String homeNumber) throws Exception {
        // deciding here on proxy for shipping
        proxyShipping.processShipping(user, store, state, city, street, homeNumber);
    }
}
