package UILayer;

import DomainLayer.Store;
import ServiceLayer.ProductService;
import ServiceLayer.StoreService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/store/:storeid")
public class StorePageUI extends VerticalLayout implements BeforeEnterObserver {

    private final StoreService storeService;
    private final ProductService productService;

    @Autowired
    public StorePageUI(StoreService configuredStoreService, ProductService configuredProductService, String storeId) {
        this.storeService = configuredStoreService;
        this.productService = configuredProductService;
        if (storeService.getStoreById(storeId).isEmpty()) {
            Store store = storeService.getStoreById(storeId).get();

            add(new HorizontalLayout(new H1(store.getName()), new Button("search in store", e -> {
                UI.getCurrent().navigate("/" + "searchproduct" + "/" + storeId);
            })), new StoreProductListUI(store.getId(), productService));
        } else {
            add(new Span("this store does not exist"));
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        RouteParameters parameters = beforeEnterEvent.getRouteParameters();
            if (parameters.get("storeid").isPresent()) {
                String storeId = parameters.get("storeid").get();
            } else {
                add(new Span("No fitting store"));
            }
    }
}
