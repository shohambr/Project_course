package DomainLayer.DomainServices;

import DomainLayer.IToken;
import InfrastructureLayer.NotificationRepository;
import InfrastructureLayer.StoreRepository;
import InfrastructureLayer.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Notifications;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToNotifyTest {
    @Mock NotificationRepository notificationRepo;
    @Mock IToken tokenService;
    @Mock NotificationWebSocketHandler notificationWebSocketHandler;
    @Mock UserRepository userRepository;
    @Mock StoreRepository storeRepository;
    @InjectMocks ToNotify toNotify;

    @BeforeEach
    void setUp() {
        toNotify = new ToNotify(notificationRepo, tokenService, notificationWebSocketHandler, userRepository, storeRepository);
    }

    @Test
    void getUserNotifications_returnsNotifications() {
        String token = "token123";
        String username = "user1";
        Notifications n1 = new Notifications("msg1", username, "store1");
        Notifications n2 = new Notifications("msg2", username, "store2");
        when(tokenService.extractUsername(token)).thenReturn(username);
        when(notificationRepo.getAll()).thenReturn(List.of(n1, n2));

        List<Notifications> result = toNotify.getUserNotifications(token);
        assertEquals(2, result.size());
        assertTrue(result.contains(n1));
        assertTrue(result.contains(n2));
    }

    @Test
    void getUserNotifications_emptyList() {
        String token = "token123";
        when(tokenService.extractUsername(token)).thenReturn("user1");
        when(notificationRepo.getAll()).thenReturn(Collections.emptyList());
        List<Notifications> result = toNotify.getUserNotifications(token);
        assertTrue(result.isEmpty());
    }

    @Test
    void getStoreNotifications_returnsMessages() {
        String storeId = "store1";
        Notifications n1 = new Notifications("msg1", "user1", storeId);
        Notifications n2 = new Notifications("msg2", "user2", storeId);
        when(notificationRepo.findByStoreID(storeId)).thenReturn(List.of(n1, n2));
        List<String> result = toNotify.getStoreNotifications(storeId);
        assertEquals(List.of("msg1", "msg2"), result);
    }

    @Test
    void getStoreNotifications_emptyList() {
        String storeId = "store1";
        when(notificationRepo.findByStoreID(storeId)).thenReturn(Collections.emptyList());
        List<String> result = toNotify.getStoreNotifications(storeId);
        assertTrue(result.isEmpty());
    }

    @Test
    void sendNotificationToStore_sendsToManagedUsers() throws Exception {
        String token = "token";
        String storeName = "StoreA";
        String storeId = "store1";
        String message = "Hello";
        RegisteredUser user1 = mock(RegisteredUser.class);
        Store store = mock(Store.class);
        when(userRepository.getAll()).thenReturn(List.of(user1));
        when(storeRepository.getAll()).thenReturn(List.of(store));
        when(store.getName()).thenReturn(storeName);
        when(store.getId()).thenReturn(storeId);
        when(user1.getManagedStores()).thenReturn(List.of(storeId));
        when(user1.getUsername()).thenReturn("user1");

        toNotify.sendNotificationToStore(token, storeName, message);
        verify(notificationWebSocketHandler).sendNotificationToClient("user1", message);
    }

    @Test
    void sendNotificationToUser_sendsNotification() throws Exception {
        String storeId = "store1";
        String userId = "user1";
        String message = "msg";
        toNotify.sendNotificationToUser(storeId, userId, message);
        verify(notificationWebSocketHandler).sendNotificationToClient(userId, message);
    }

    @Test
    void sendNotificationToUser_throwsException() {
        String storeId = "store1";
        String userId = "user1";
        String message = "msg";
        doThrow(new RuntimeException("fail")).when(notificationWebSocketHandler).sendNotificationToClient(userId, message);
        Exception ex = assertThrows(RuntimeException.class, () -> toNotify.sendNotificationToUser(storeId, userId, message));
        assertTrue(ex.getMessage().contains("Failed to serialize notification"));
    }

    // sendAllUserNotifications is hard to test due to missing notifications field in ToNotify
    // You may want to refactor ToNotify to inject notifications or make it accessible for testing
} 