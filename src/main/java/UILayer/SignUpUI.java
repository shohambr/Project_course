package UILayer;

import DomainLayer.Roles.RegisteredUser;
import ServiceLayer.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/signup")
public class SignUpUI extends VerticalLayout {

    private final UserService userService;

    @Autowired
    public SignUpUI(UserService configuredUserService) {
        this.userService = configuredUserService;

        TextField username = new TextField("username");
        PasswordField password = new PasswordField("password");
        Span error = new Span("");
        Button login = new Button("sign up", e -> {
            try {
                userService.signUp(username.getValue(), password.getValue());
                UI.getCurrent().navigate("");
            } catch (Exception exception) {
                error.setText(exception.getMessage());
            }
        });
        add(new H2("sign up"), username, password, login, error);
        setAlignItems(FlexComponent.Alignment.CENTER);
    }

}
