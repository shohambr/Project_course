package DomainLayer;
import DomainLayer.Roles.RegisteredUser;
import ServiceLayer.UserService;

public abstract class User {
    protected int id = 1;
    protected ShoppingCart shoppingCart;
    protected String myToken;
    protected UserService userService;

    public User() {
        this.shoppingCart = new ShoppingCart(id++);
    }

    public User(UserService userService) {
        this.userService = userService;
        this.shoppingCart = new ShoppingCart(id++);
    }

    public void addProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.addProduct(store , product);
    }

    public void removeProduct(Store store, Product product){    //Store helps shopping cart to know to what shopping bag
        shoppingCart.removeProduct(store, product);
    }

    public boolean purchaseCart(){
        try{
            userService.purchaseCart(id , myToken , shoppingCart);
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public User login(String username , String pass){
        try{
            return new RegisteredUser(userService.login(username , pass));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public abstract void logout();

    public RegisteredUser register(String username , String pass) throws Exception{
        return new RegisteredUser(userService.signUp(username , pass));
    }

    public String getToken() {
        return myToken;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}

