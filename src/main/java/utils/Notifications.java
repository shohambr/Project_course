package utils;

public class Notifications {
    private String message;
    private String userId;
    private String storeId;

    public Notifications() {
    }

    public Notifications(String message, String userId, String storeId) {
        this.message = message;
        this.userId = userId;
        this.storeId = storeId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
