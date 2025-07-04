package UILayer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/edit-store")
public class EditStorePageUI extends VerticalLayout {

    public EditStorePageUI() {
        // Store Selection and Sign-out Section
        ComboBox<String> storeDropdown = new ComboBox<>("Store");
        storeDropdown.setItems("Store 1", "Store 2", "Store 3"); // Example store names
        storeDropdown.setPlaceholder("Select Store");

        Button signOutButton = new Button("Sign Out", e -> {
            // Handle sign-out logic here (e.g., session invalidate)
        });

        HorizontalLayout topBar = new HorizontalLayout(
                new H2("Store Manager Dashboard"),
                storeDropdown,
                signOutButton
        );
        topBar.setAlignItems(Alignment.CENTER);
        add(topBar);

        // Add New Product Section
        TextField productName = new TextField("Product Name");
        TextField productDescription = new TextField("Description");
        NumberField productPrice = new NumberField("Price");
        NumberField productQuantity = new NumberField("Quantity");
        NumberField productRating = new NumberField("Rating");
        TextField productCategory = new TextField("Category");

        Button addProductButton = new Button("Add Product", e -> {
            // Add product logic here (using Product constructor)
        });

        VerticalLayout addProductForm = new VerticalLayout(
                new Span("Add New Product"),
                productName,
                productDescription,
                productPrice,
                productQuantity,
                productRating,
                productCategory,
                addProductButton
        );

        // Set Add Product Form Styling and Padding
        addProductForm.setPadding(true);
        addProductForm.setAlignItems(Alignment.CENTER);
        add(addProductForm);

        // Set New Discount Section
        NumberField discountLevel = new NumberField("Discount Level");
        NumberField logicComposition = new NumberField("Logic Composition");
        NumberField numericalComposition = new NumberField("Numerical Composition");
        NumberField percentDiscount = new NumberField("Percent Discount");
        TextField discountedItem = new TextField("Discounted Item");
        NumberField discountCondition = new NumberField("Condition");
        NumberField discountLimiter = new NumberField("Limiter");
        TextField conditionalDiscounted = new TextField("Conditional Discounted");

        Button addDiscountButton = new Button("Add Discount", e -> {
            // Add discount logic here (using Discount constructor)
        });

        VerticalLayout addDiscountForm = new VerticalLayout(
                new Span("Set New Discount"),
                discountLevel,
                logicComposition,
                numericalComposition,
                percentDiscount,
                discountedItem,
                discountCondition,
                discountLimiter,
                conditionalDiscounted,
                addDiscountButton
        );

        // Set Add Discount Form Styling and Padding
        addDiscountForm.setPadding(true);
        addDiscountForm.setAlignItems(Alignment.CENTER);
        add(addDiscountForm);

        // Notification Example for Adding Product/Discount
        Notification notification = new Notification("Product or Discount Added!", 3000);
        notification.open();
    }
}
