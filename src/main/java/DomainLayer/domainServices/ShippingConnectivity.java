package DomainLayer.DomainServices;

import DomainLayer.*;

import java.util.List;
import java.util.Map;

public class ShippingConnectivity {

    private IShipping proxyShipping;

    public ShippingConnectivity(IShipping proxyShipping) {
        this.proxyShipping = proxyShipping;
    }

    public void processShipping(User user, String state, String city, String street, String homeNumber) throws Exception {
        // shoulod have choice deciding here on proxy for shipping
        List<ShoppingBag> shoppingBags = user.getShoppingCart().getShoppingBags();
        for (ShoppingBag shoppingBag : shoppingBags) {
            proxyShipping.processShipping(user.getID(), state, city, street, shoppingBag.getProducts(), homeNumber);
        }
    }
}
