package DomainLayer;

public interface IOrderRepository {
    void addOrder(Order order);
    void removeOrder(Order order);
}