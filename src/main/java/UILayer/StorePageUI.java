package UILayer;

import DomainLayer.Store;
import ServiceLayer.ProductService;
import ServiceLayer.StoreService;
import ch.qos.logback.core.encoder.EchoEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Route("/store/:storeid")
public class StorePageUI extends VerticalLayout implements BeforeEnterObserver {

    private final StoreService storeService;
    private final ProductService productService;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public StorePageUI(StoreService configuredStoreService, ProductService configuredProductService, String storeId) {
        this.storeService = configuredStoreService;
        this.productService = configuredProductService;
        if (storeService.getStoreById(storeId).isEmpty()) {
            try {
                Store store = mapper.readValue(storeService.getStoreById(storeId).get(), Store.class);
                add(new HorizontalLayout(new H1(store.getName()), new Button("search in store", e -> {
                    UI.getCurrent().navigate("/" + "searchproduct" + "/" + storeId);
                })), new StoreProductListUI(store.getId(), productService));
            } catch (Exception e) {
                Notification.show(e.getMessage());
            }
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
