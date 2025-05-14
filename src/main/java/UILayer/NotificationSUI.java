package UILayer;

import DomainLayer.IToken;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import infrastructureLayer.NotificationClientRepository;
import infrastructureLayer.NotificationServerRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/notification")
public class NotificationSUI extends VerticalLayout {

    private NotificationClientRepository notificationClientRepository;
    private NotificationServerRepository notificationServerRepository;
    private IToken tokenService;

    @Autowired
    public NotificationSUI(NotificationClientRepository configuredNotificationClientRepository, NotificationServerRepository configuredNotificationServerRepository, IToken configuredTokenService) {
        this.notificationClientRepository = configuredNotificationClientRepository;
        this.notificationServerRepository = configuredNotificationServerRepository;
        this.tokenService = configuredTokenService;
        TextField username = new TextField("username");
        TextField notification = new TextField("notification to send");
        add(username);

        add(notification);

        add(new Button("send notification", e -> {String token = tokenService.generateToken(username.getValue());
        }))
    }
}
