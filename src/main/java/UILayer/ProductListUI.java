package UILayer;

import DomainLayer.Product;
import DomainLayer.ShoppingBag;
import DomainLayer.ShoppingCart;
import ServiceLayer.ProductService;
import ServiceLayer.StoreService;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ProductListUI extends HorizontalLayout {

    private final ShoppingCart shoppingCart;
    private final ProductService productService;
    private final StoreService storeService;
    @Autowired
    public ProductListUI(ShoppingCart configuredShoppingCart, ProductService configuredProductService, StoreService configuredStoreService) {
        this.shoppingCart = configuredShoppingCart;
        this.productService = configuredProductService;
        this.storeService = configuredStoreService;
        add(new VerticalLayout(new Span("store"), new Span("product\namount\nprice")));
        double totalPayment = 0;
        for (ShoppingBag shoppingBag: shoppingCart.getShoppingBags()) {
            VerticalLayout productList = new VerticalLayout();
            productList.add(new Span(storeService.getStoreName(shoppingBag.getStoreId())));
            for (Map.Entry<String, Integer> product : shoppingBag.getProducts().entrySet()) {
                productList.add(new Span(productService.getProductById(product.getKey()).get().getName() + "\n" + product.getValue() + "\n" +productService.getProductById(product.getKey()).get().getPrice()));
                totalPayment = totalPayment + product.getValue()  * productService.getProductById(product.getKey()).get().getPrice();
            }
            add(productList, new Span("total payment :" + totalPayment));
        }

    }

}
