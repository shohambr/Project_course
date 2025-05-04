package ServiceLayer;

import DomainLayer.IOrderRepository;
import DomainLayer.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class OrderServiceTest {

    private IOrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = Mockito.mock(IOrderRepository.class);
        orderService = new OrderService(orderRepository);
    }

    @Test
    void addOrder_ShouldCallAddOrder() {
        Order order = new Order("blah1", "1", 10.0);

        orderService.addOrder(order);

        verify(orderRepository, times(1)).addOrder(order);
    }

    @Test
    void removeOrder_ShouldCallRemoveOrder_WhenOrderExists() {
        Order order = new Order("blah2", "2", 20.0);
        orderService.addOrder(order);

        orderService.removeOrder(order);

        verify(orderRepository, times(1)).removeOrder(order);
    }
}
