package UILayer;

import DomainLayer.ShoppingCart;
import DomainLayer.User;
import ServiceLayer.ProductService;
import ServiceLayer.RegisteredService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route("/shoppingcart")
public class ShoppingCartUI extends VerticalLayout {

    private final RegisteredService registeredService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ShoppingCartUI(RegisteredService configuredRegisteredService, ProductService configuredProductService, UserService configuredUserService) {
        this.registeredService = configuredRegisteredService;
        this.productService = configuredProductService;
        this.userService = configuredUserService;
        Button signOut = new Button("Sign out", e -> {
            try {
                String token = (String) UI.getCurrent().getSession().getAttribute("token");
                UI.getCurrent().getSession().setAttribute("token", registeredService.logoutRegistered(token));
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

        add(new ProductListUI(shoppingCart, productService, userService));

        add(new Button("purchase cart", e -> {UI.getCurrent().navigate("/purchasecart");}));

        setPadding(true);
        setAlignItems(Alignment.CENTER);

    }
}
