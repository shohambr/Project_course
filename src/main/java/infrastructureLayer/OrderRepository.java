package infrastructureLayer;
import DomainLayer.IOrderRepository;
import DomainLayer.Order;

import java.util.HashMap;
import java.util.Map;

public class OrderRepository implements IOrderRepository {
    private Map<Order, String> Orders = new HashMap<>();

    public void addOrder(Order order) {
        for (Order existingOrder : Orders.keySet()) {
            if (existingOrder.getId().equals(order.getId())) {
                return;
            }
        }
        Orders.put(order, order.getId());
    }


    public void removeOrder(Order order) {
        Order orderToRemove = null;
        for (Order existingOrder : Orders.keySet()) {
            if (existingOrder.getId().equals(order.getId())) {
                orderToRemove = existingOrder;
                break;
            }
        }

        if (orderToRemove != null) {
            Orders.remove(orderToRemove);
        }
    }

}
