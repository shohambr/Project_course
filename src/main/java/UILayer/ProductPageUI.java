package UILayer;

import DomainLayer.Product;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;
import ServiceLayer.ProductService;
import ServiceLayer.StoreService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/product/:productid/:storeid")
public class ProductPageUI extends VerticalLayout implements BeforeEnterObserver {

    private ProductService productService;
    private StoreService storeService;
    private UserService userService;

    @Autowired
    public ProductPageUI(ProductService configuredProductService, StoreService configuredStoreService, UserService configuredUserService, String productId, String storeId) {
        this.productService = configuredProductService;
        this.storeService = configuredStoreService;
        this.userService = configuredUserService;
        if (productService.getProductById(productId).isPresent()) {
            Product product = productService.getProductById(productId).get();
            Store store = storeService.getStoreById(storeId);
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
                RegisteredUser user = (RegisteredUser) UI.getCurrent().getSession().getAttribute("user");
                UI.getCurrent().navigate("/:userid");
            });

            HorizontalLayout upwardsPage = new HorizontalLayout(signOut, new H1(product.getName()),new H1(store.getName()), homePage);
            upwardsPage.setAlignItems(Alignment.CENTER);

            add(upwardsPage);

            Button addToCart = new Button("add to cart", e -> {
                RegisteredUser user = (RegisteredUser) UI.getCurrent().getSession().getAttribute("user");
                Notification.show(userService.addToCart(user.getToken(), user, store, product));
            });

            HorizontalLayout bottomDescription = new HorizontalLayout(new Span(product.getDescription()), new Span("" + product.getPrice()));

            add(new VerticalLayout(addToCart, bottomDescription));

            setPadding(true);
            setAlignItems(Alignment.CENTER);

        } else {
            add(new Span("No product with id:" + productId));
        }

    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        RouteParameters parameters = beforeEnterEvent.getRouteParameters();
            if(parameters.get("productid").isPresent()) {
                String productId = (String) parameters.get("productid").get();
                if (parameters.get("storeid").isPresent()) {
                    String storeId = parameters.get("storeid").get();
                } else {
                    add(new Span("No fitting store"));
                }
            } else {
                add(new Span("No fitting product"));
            }
    }
}
