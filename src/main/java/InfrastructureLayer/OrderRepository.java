package InfrastructureLayer;
import DomainLayer.IOrderRepository;
import DomainLayer.Order;
import ServiceLayer.EventLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository implements IOrderRepository {
    private Map<String, List<String>> Orders = new HashMap<>();
    private static OrderRepository instance;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderRepository() {}

    // Public method to provide access to the singleton instance
    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public void save(Order order) {
        try {
            String orderJson = mapper.writeValueAsString(order);
            // Assuming the order has methods to get storeId and userId
            String storeId = order.getStoreId();
            String userId = order.getUserId();
            addOrder(orderJson, storeId, userId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to save order", e);
        }
    }

    @Override
    public void addOrder(String order, String storeId, String userId) {
        List<String> orderByStore = Orders.get(storeId);
        if(orderByStore == null) {
            List<String> lst = new ArrayList<>();
            lst.add(order);
            Orders.put(storeId, lst);
        }
        else {
            List<String> lst = orderByStore;
            lst.add(order);
            Orders.put(storeId, lst);
        }

        List<String> orderByUser = Orders.get(userId);
        if(orderByUser == null) {
            List<String> lst = new ArrayList<>();
            lst.add(order);
            Orders.put(userId, lst);
        }
        else {
            List<String> lst = orderByUser;
            lst.add(order);
            Orders.put(userId, lst);
        }
        EventLogger.logEvent("addOrder", "Order added successfully");
    }

    @Override
    public void removeOrder(String order, String storeId, String userId) {
        List<String> orderByStore = Orders.get(storeId);
        if(orderByStore != null) {
            orderByStore.remove(order);
            Orders.put(storeId, orderByStore);
        }

        List<String> orderByUser = Orders.get(userId);
        if(orderByUser != null) {
            orderByUser.remove(order);
            Orders.put(userId, orderByUser);
        }
        EventLogger.logEvent("removeOrder", "Order removed successfully");
    }

    @Override
    public List<String> getOrderByStoreId(String storeId) {
        return Orders.getOrDefault(storeId, new ArrayList<>());
    }

    @Override
    public List<String> getOrderByUserId(String userId) {
        return Orders.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public List<String> getOrderHistory(String userId) {
        List<String> lst = this.Orders.get(userId);
        List<String> clone = new ArrayList<>();
        clone.addAll(lst);
        return clone;
    }
}