package UILayer;

import DomainLayer.*;
import DomainLayer.Roles.Jobs.Job;
import ServiceLayer.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import infrastructureLayer.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route("")
public class Login extends VerticalLayout {

    private final UserService userService;

    @Autowired
    public Login(UserService configured_userService) {
        this.userService = configured_userService;

        try {
            userService.signUp("username", "password");
        } catch (Exception e) {

        }
        TextField username = new TextField("username");
        PasswordField password = new PasswordField("password");
        Span error = new Span("");
        Button login = new Button("login",e -> {
            try {
                userService.login(username.getValue(), password.getValue());
                UI.getCurrent().navigate("/pages");
            } catch (Exception exception) {
                error.setText(exception.getMessage());
            }
        });
        add(new H2("login"), username, password, login, error);
        setAlignItems(Alignment.CENTER);
    }
}
