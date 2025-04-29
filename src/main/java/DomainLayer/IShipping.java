package DomainLayer;

import java.util.List;
import java.util.Map;

public interface IShipping {
    void processShipping(User user, Store store, String state, String city, String street, Map<Product, Integer> products, String homeNumber) throws Exception;
}