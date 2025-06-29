package UILayer;

import DomainLayer.*;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.domainServices.DiscountPolicyMicroservice;
import InfrastructureLayer.StoreRepository;
import ServiceLayer.ProductService;
import ServiceLayer.RegisteredService;
import ServiceLayer.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("/purchasecart")
public class PurchaseCartUI extends VerticalLayout {

    private final ProductService productService;
    private final RegisteredService registeredService;
    private final IProductRepository productRepository;
    private final IToken tokenService;
    private final IUserRepository userRepository;
    private final IDiscountRepository discountRepository;
    private final StoreRepository storeRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public PurchaseCartUI(ProductService productService, RegisteredService registeredService, IProductRepository productRepository, IToken tokenService, IUserRepository userRepository, StoreRepository storeRepository, IDiscountRepository discountRepository) {
        this.productService = productService;
        this.registeredService = registeredService;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        String token = (String) UI.getCurrent().getSession().getAttribute("token");
        String username = tokenService.extractUsername(token);
        String jsonUser = userRepository.getUser(username);
        RegisteredUser user = null;
        try {
            user = mapper.readValue(jsonUser, RegisteredUser.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Buttons and Navigation
        Button signOut = new Button("🔐 Sign out", e -> {
            try {
                UI.getCurrent().getSession().setAttribute("token", registeredService.logoutRegistered(token));
                UI.getCurrent().navigate("");
            } catch (Exception exception) {
                Notification.show(exception.getMessage());
            }
        });

        Button homePage = new Button("🏠 Home", e -> {
            UI.getCurrent().navigate("");
        });

        Button confirmPurchase = new Button("✅ Confirm Purchase", e -> {
            // Logic for confirming purchase
            Notification.show("Purchase Confirmed!");
        });

        Button cancelPurchase = new Button("❌ Cancel Purchase", e -> {
            // Logic for canceling purchase
            Notification.show("Purchase Canceled!");
        });

        // Add to layout
        HorizontalLayout topLayout = new HorizontalLayout(signOut, new H1("Shopping Cart"), homePage);
        add(topLayout);

        // Get User Data (Assuming logged in user is saved in session)
        ShoppingCart shoppingCart = user.getShoppingCart();

        // Display Product Grid
        Grid<Product> productGrid = new Grid<>(Product.class);
        List<ShoppingBag> shoppingBags = shoppingCart.getShoppingBags();
//        for (ShoppingBag shoppingBag : shoppingBags) {
//            productGrid.setItems(); // creat presentor**
//        }
        productGrid.setColumns("name", "quantity", "description", "price");

        // Total price section
        TextField totalField = new TextField();
        totalField.setLabel("Total");
        double payment = 0;
        for (ShoppingBag shoppingBag : shoppingBags) {
            DiscountPolicyMicroservice discountPolicy = new DiscountPolicyMicroservice(storeRepository, userRepository, productRepository, discountRepository);
            Map<Product, Integer> products = new HashMap<Product, Integer>();
            for (String product : shoppingBag.getProducts().keySet()) {
                products.put(productRepository.getReferenceById(product), shoppingBag.getProducts().get(product));
            }

            Product firstProduct = products.keySet().iterator().next();
            Map<String, Integer> productsString = new HashMap<>();
            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                String storeId = entry.getKey().getStoreId();  // Extract key
                productsString.put(storeId, entry.getValue()); // Preserve value
            }
            payment = payment + discountPolicy.calculatePrice(firstProduct.getStoreId(), productsString);
        }

        totalField.setValue("$" + payment); // Assuming getTotalPrice() exists
        totalField.setReadOnly(true);

        // Buttons for Confirm/Cancel
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmPurchase, cancelPurchase);
        add(productGrid, totalField, buttonLayout);

        setPadding(true);
        setAlignItems(Alignment.CENTER);
        this.storeRepository = storeRepository;
    }
}
