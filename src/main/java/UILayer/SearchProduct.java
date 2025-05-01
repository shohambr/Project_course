package UILayer;

import DomainLayer.Product;
import DomainLayer.User;
import ServiceLayer.StoreService;
import ServiceLayer.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Route("/searchproduct")
public class SearchProduct extends VerticalLayout {

    private final UserService userService;

    @Autowired
    public SearchProduct(UserService configuredUserService) {
        this.userService = configuredUserService;

        TextField productName = new TextField("product name");
        Button searchProduct = new Button("search product", e -> {
            try {
                User user = (User) UI.getCurrent().getSession().getAttribute("user");
                List<String> items = userService.searchItems(productName.getValue(), user.getToken());
                List<Product> products = items.stream().map(item -> {
                    try {
                        return new ObjectMapper().readValue(item, Product.class);
                    } catch (Exception exception) {
                        return null;
                    }
                }).filter(Objects::nonNull).toList();
                for (Product product : products) {
                    add(new Button(product.getName() + "\n" + product.getPrice(), choose -> {UI.getCurrent().navigate("product/" + product.getStoreId() + "/" + product.getId());}));
                }
            } catch (Exception exception) {
                Notification.show(exception.getMessage());
            }
        });

        add(productName, searchProduct);
    }
}
