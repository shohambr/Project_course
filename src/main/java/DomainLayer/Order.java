package DomainLayer;
import java.util.*;

public class Order {
    private String id;
    private final String info;
    private String storeId;
    private String userId;
    private Date date;

    // the date here is in orgument for the tests if we would want to change it
    public Order(String info, String storeId, String userId, String id, Date date) {
        this.storeId = storeId;
        this.userId = userId;
        this.id = id;
        this.info = info;
        this.date = date;
    }
    // Getters
    public String getId() {
        return id;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getUserId() {
        return userId;
    }

    public String getInfo() {
        return info;
    }

    public Date getDate() {
        return date;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        //todo
        return "";
    }
}