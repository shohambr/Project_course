package DomainLayer;

import java.util.List;

public interface IOrderRepository {
    void addOrder(String order, String storeId, String userId);
    void removeOrder(String order, String storeId, String userId);
    List<String> getOrderByStoreId(String storeId);
    List<String> getOrderByUserId(String userId);
    List<String> getOrderHistory(String username);

}