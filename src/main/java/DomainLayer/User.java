package DomainLayer;
import DomainLayer.Roles.RegisteredUser;
import ServiceLayer.UserService;

public abstract class User {
    private int id;
    private ShoppingCart shoppingCart;
    private String myToken;
    protected UserService userService;

    public User() {
        this.shoppingCart = new ShoppingCart(id);
    }

    public void addProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.addProduct(store , product);
    }

    public void removeProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.removeProduct(store, product);
    }

    public void purchaseCart(){
        userService.purchaseCart(id , myToken , shoppingCart);
    }

    public User login(String username , String pass){
        try{
            return new RegisteredUser(userService.login(username , pass));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String logout(String username , String pass){
        return userService.login(username , pass);
    }

    public String register(String username , String pass){
        return userService.login(username , pass);
    }

}

