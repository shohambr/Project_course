package DomainLayer;
import java.util.*;

public class Order {
    private String id;
    private final String info;

    public Order(String info ) {
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}