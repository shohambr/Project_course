package DomainLayer;
import DomainLayer.Roles.Role;
import ServiceLayer.TokenService;

public class User {
    private int id;
    private String name;                 //No need
    private Role role;
    private ShoppingCart shoppingCart;


    public User(int id, String name, Role role, ShoppingCart shoppingCart) {
        this.id = id;            //Should be automatic
        this.name = name;
        this.role = role;
        this.shoppingCart = shoppingCart;
    }

    public void addProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.addProduct(store, product);
    }

    public void removeProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.removeProduct(store, product);
    }


    public void signUp(String username, String password){
        TokenService.signUp(username, password);
    }               //Should be instead UserService.signUp...

    public void Login(String username, String password){
        TokenService.login(username, password);
    }      //Should be instead UserService.login...

}

