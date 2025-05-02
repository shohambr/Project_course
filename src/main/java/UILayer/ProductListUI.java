package UILayer;

import DomainLayer.Product;
import DomainLayer.ShoppingBag;
import DomainLayer.ShoppingCart;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ProductListUI extends HorizontalLayout {

    private final ShoppingCart shoppingCart;
    @Autowired
    public ProductListUI(ShoppingCart configuredShoppingCart) {
        this.shoppingCart = configuredShoppingCart;

        add(new VerticalLayout(new Span("store"), new Span("product\namount\nprice")));

        for (ShoppingBag shoppingBag: shoppingCart.getShoppingBags()) {
            VerticalLayout productList = new VerticalLayout();
            productList.add(new Span(shoppingBag.getStore().getName()));
            for (Map.Entry<Product, Integer> product : shoppingBag.getProducts().entrySet()) {
                productList.add(new Span(product.getKey().getName() + "\n" + product.getValue() + "\n" + product.getKey().getPrice()));
            }
            add(productList);
        }

    }

}
