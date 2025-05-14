package infrastructureLayer;

import jakarta.websocket.WebSocketContainer;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;

//@Repository
//@ServerEndpoint("/server")
//public class NotificationServerRepository {
//
//    private HashMap<String, WebSocketContainer> clientWebSockets = new HashMap<String, WebSocketContainer>();
//
//    public NotificationServerRepository() {}
//
//    public void addClientWebSocket(String token, WebSocketContainer clientWebSocket) {
//        clientWebSockets.put(token, clientWebSocket);
//    }
//
//    public void sendNotificationToClient(String token, String notificationToClient) {
//        for (String tokens : clientWebSockets.keySet()) {
//            if (tokens.equals(token)) {
//                SimpMessagingTemplate simpleMessage = new Simpl
//                clientWebSockets.get(tokens)
//            }
//        }
//    }
//
//}
