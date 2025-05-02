package UILayer;

import DomainLayer.Product;
import DomainLayer.Store;
import DomainLayer.User;
import ServiceLayer.StoreService;
import ServiceLayer.UserService;
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

@Route("/searchstore")
public class SearchStoreUI extends VerticalLayout {

    private final StoreService storeService;

    @Autowired
    public SearchStoreUI(StoreService configuredStoreService) {

        this.storeService = configuredStoreService;

        TextField storeName = new TextField("store name");
        Button searchStore = new Button("search store", e -> {
            try {
                User user = (User) UI.getCurrent().getSession().getAttribute("user");
                List<String> items = storeService.searchStores(storeName.getValue(), user.getToken());
                List<Store> stores = items.stream().map(item -> {
                    try {
                        return new ObjectMapper().readValue(item, Store.class);
                    } catch (Exception exception) {
                        return null;
                    }
                }).filter(Objects::nonNull).toList();
                for (Store store : stores) {
                    add(new Button(store.getName() , choose -> {UI.getCurrent().navigate("/store/" + store.getId());}));
                }
            } catch (Exception exception) {
                Notification.show(exception.getMessage());
            }
        });

        add(storeName, searchStore);

    }
}
