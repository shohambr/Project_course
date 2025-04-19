package DomainLayer;
import DomainLayer.Roles.RegisteredUser;
import ServiceLayer.UserService;

public abstract class User {
    protected String id = "1";
    protected ShoppingCart shoppingCart;
    protected String myToken;

    public User() {
        this.shoppingCart = new ShoppingCart((id));
    }

    public void addProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.addProduct(store , product);
    }

    public void removeProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.removeProduct(store, product);
    }

    public String getToken() {
        return myToken;
    }
    public String getID() {
        return this.id;
    }

}