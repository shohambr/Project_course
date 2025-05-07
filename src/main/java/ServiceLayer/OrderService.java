package ServiceLayer;
import DomainLayer.IOrderRepository;
import DomainLayer.Order;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService{
    IOrderRepository OrderRepository;
    private String id = "1";


    public OrderService(IOrderRepository OrderRepository) {
        this.OrderRepository = OrderRepository;
    }

    public void addOrder(Order order){
        order.setId(id);
        int numericId = Integer.parseInt(id);
        numericId++;
        id = String.valueOf(numericId);
        OrderRepository.addOrder(order);
    }

    public void removeOrder(Order order){
        OrderRepository.removeOrder(order);
    }
}