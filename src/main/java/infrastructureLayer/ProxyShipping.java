package infrastructureLayer;

import DomainLayer.IShipping;
import DomainLayer.Store;
import DomainLayer.User;

public class ProxyShipping implements IShipping {

    public ProxyShipping() {}
    public void processShipping(User user, Store store, String state, String city, String street, String homeNumber) throws Exception {

    }

}
