package DomainLayer;

import java.util.List;
import java.util.Map;

public interface IShipping {
    void processShipping(String userId, String state, String city, String street, Map<String, Integer> products, String homeNumber) throws Exception;
}