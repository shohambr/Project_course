package DomainLayer;
import java.util.*;

public class Order {
    private int id;
    double totalPrice;
    String buyerId;
    String storeId;
    String shippingAddress;

    public Order(int id, double totalPrice, String buyerId, String storeId, String shippingAdress) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }
}