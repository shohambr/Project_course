package UILayer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/pages")
public class MorePagesUI extends VerticalLayout {
    public MorePagesUI() {
        TextField textField = new TextField("vbduis");
        Span label = new Span("a");
        //button puts textfield value in label
        Button button = new Button("one two three", e -> {label.setText(textField.getValue());});
        VerticalLayout column1 = new VerticalLayout(new Span("a"), new Span("b"));
        VerticalLayout column2 = new VerticalLayout(new Span("a"), new Span("b"));
        HorizontalLayout columns = new HorizontalLayout(column1, column2);
        columns.setAlignItems(Alignment.CENTER);
        add(new HorizontalLayout(new H2("Title"), button), columns, textField, label);
        setPadding(true);
        setAlignItems(Alignment.CENTER);
        Span message = new Span("🔔 You have a new message!");
        Button actionButton = new Button("View Details", e -> {
            Notification.show("You clicked inside the notification!");
        });
        Notification notification = new Notification();
        notification.setDuration(0); // Stay visible until user interacts
        notification.setPosition(Notification.Position.TOP_END);
        HorizontalLayout layout = new HorizontalLayout(message, actionButton);
        layout.setAlignItems(Alignment.CENTER);
        notification.add(layout);
        notification.open();

    }
}
