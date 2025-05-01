package UILayer;

import DomainLayer.Roles.RegisteredUser;
import DomainLayer.ShoppingCart;
import DomainLayer.User;
import ServiceLayer.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route("/shoppingcart")
public class ShoppingCartUI extends VerticalLayout {

    private final UserService userService;

    @Autowired
    public ShoppingCartUI(UserService configuredUserService) {
        this.userService = configuredUserService;
        Button signOut = new Button("Sign out", e -> {
            try {
                RegisteredUser user = (RegisteredUser) UI.getCurrent().getSession().getAttribute("user");
                UI.getCurrent().getSession().setAttribute("user", userService.logoutRegistered(user.getToken(), user));
                UI.getCurrent().navigate("");
            } catch (Exception exception) {
                Notification.show(exception.getMessage());
            }
        }
        );

        Button homePage = new Button("Home page", e -> {
            UI.getCurrent().navigate("");
        });

        add(new HorizontalLayout(signOut, new H1("Shopping cart"), homePage));

        User user = (User) UI.getCurrent().getSession().getAttribute("user");

        ShoppingCart shoppingCart = user.getShoppingCart();

        add(new ProductListUI(shoppingCart));

        add(new Span("Price" + shoppingCart.calculatePurchaseCart()));

        setPadding(true);
        setAlignItems(Alignment.CENTER);

    }
}
