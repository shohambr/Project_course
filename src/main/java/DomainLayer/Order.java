package DomainLayer;

import java.util.*;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id = UUID.randomUUID().toString();

    @Column(name = "info", nullable = false, columnDefinition = "TEXT")
    private final String info;

    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", nullable = false)
    private Date date;

    // the date here is in argument for the tests if we would want to change it
    public Order(String info, String storeId, String userId, Date date) {
        this.storeId = storeId;
        this.userId = userId;
        this.info = info;
        this.date = date;
    }

    // Required by JPA
    protected Order() {
        this.info = null;
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