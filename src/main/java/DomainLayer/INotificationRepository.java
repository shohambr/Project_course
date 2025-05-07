package DomainLayer;

import java.util.List;
public interface INotificationRepository {

    public void addNotification(String recieverId, String notification) throws Exception;
    public void removeNotification(String recieverId, String notification) throws Exception;
    public void clearNotifications(String recieverId);
    public void clearAllNotifications();
    public List<String> getMyNotification(String recieverId);
}
