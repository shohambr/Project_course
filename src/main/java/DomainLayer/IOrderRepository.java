package DomainLayer;

import java.util.List;

public interface IOrderRepository {
    void addOrder(Order order);
    void removeOrder(Order order);
    List<String> getOrderHistory(String userId) throws Exception;
}