package DomainLayer;
import java.util.*;

public class Order {
    private String id;
    private final String info;
    private final Double price;

    public Order(String info , String id , Double price) {
        this.info = info;
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}