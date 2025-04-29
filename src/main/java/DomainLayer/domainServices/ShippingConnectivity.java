package DomainLayer.domainServices;

import DomainLayer.IShipping;
import DomainLayer.Store;
import DomainLayer.User;

import java.util.List;

public class ShippingConnectivity {

    private IShipping proxyShipping;

    public ShippingConnectivity(IShipping proxyShipping) {
        this.proxyShipping = proxyShipping;
    }

    public void processShipping(User user, List<Store> stores, String state, String city, String street, String homeNumber) throws Exception {
        // deciding here on proxy for shipping
        for (Store store: stores) {
            proxyShipping.processShipping(user, store, state, city, street, homeNumber);
        }
    }
}
