package infrastructureLayer;

import DomainLayer.IShipping;
import DomainLayer.Product;
import DomainLayer.Store;
import DomainLayer.User;

import java.util.List;
import java.util.Map;

public class ProxyShipping implements IShipping {
    public ProxyShipping() {}
    public void processShipping(User user, Store store, String state, String city, String street, Map<Product, Integer> products, String homeNumber) throws Exception {
        //based on shipping service
    }

}
