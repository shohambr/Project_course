//package UILayer;
//
//import ServiceLayer.TokenService;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.Route;
//import infrastructureLayer.NotificationClientRepository;
//import infrastructureLayer.NotificationWebSocketHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Route("/notification")
//public class NotificationSUI extends VerticalLayout {
//
//    private TokenService tokenService;
//    private NotificationClientRepository notificationClientRepository;
//
//    @Autowired
//    public NotificationSUI(TokenService configuredTokenService, NotificationClientRepository configuredNotificationClientRepository, NotificationWebSocketHandler configuredNotificationWebSocketHandler) {
//        this.tokenService = configuredTokenService;
//        this.notificationClientRepository = configuredNotificationClientRepository;
//        TextField username = new TextField("username");
//        TextField notification = new TextField("notification to send");
//        add(username);
//
//        add(notification);
//
//        add(new Button("send notification", e -> {String token = tokenService.getToken(username.getValue());
//            notificationClientRepository.connectToServer(token);
//            notificationClientRepository.sendNotification(notification.getValue());
//        }));
//    }
//}
