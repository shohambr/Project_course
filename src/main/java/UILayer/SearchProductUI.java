package UILayer;

import DomainLayer.Product;
import DomainLayer.User;
import ServiceLayer.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Route("/searchproduct")
public class SearchProductUI extends VerticalLayout {

    private final ProductService productService;

    @Autowired
    public SearchProductUI(ProductService configuredProductService) {
        this.productService = configuredProductService;

        TextField productName = new TextField("product name");
        Button searchProduct = new Button("search product by name", e -> {
            try {
                String token = (String) UI.getCurrent().getSession().getAttribute("token");
                Optional<Product> items = productService.getProductByName(productName.getValue());
                List<Product> products = items.stream().map(item -> {
                    try {
                        return item;
                    } catch (Exception exception) {
                        return null;
                    }
                }).toList();
                for (Product product : products) {
                    add(new Button(product.getName() + "\n" + product.getPrice(), choose -> {UI.getCurrent().navigate("/product/" + product.getId() + "/" + product.getStoreId());}));
                }
            } catch (Exception exception) {
                Notification.show(exception.getMessage());
            }
        });

        add(productName, searchProduct);
    }
}
