package ServiceLayer;
import DomainLayer.IUserRepository;

import DomainLayer.ShoppingCart;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    //Should hold IUserRepository (that practically will always be UserRepository)
    private BCrypt passwordEncoder;
    private TokenService tokenService;
    private IUserRepository userRepo;

    public UserService(IUserRepository repository, TokenService tokenService) {
        this.userRepo = repository;
        this.tokenService = tokenService;
    }

    public String login(String username, String password) {
        if(!userRepo.isUserExist(username)) {
            return "username already exists";
        }
        if(passwordEncoder.checkpw(password, userRepo.getUserPass(username))) {
            return userRepo.getUser(username);
        }
        return null;
    }

    public String signUp(String username, String password ){
        if(userRepo.isUserExist(username)) {
            return "username already exists";
        }

        String hashedPassword = passwordEncoder.hashpw(password ,passwordEncoder.gensalt());
        userRepo.addUser(username , hashedPassword);
        String token = tokenService.generateToken(username);
        return token;
    }

    public void purchaseCart(int id, String myToken, ShoppingCart shoppingCart) {
        //to implement
    }

    public void logoutRegistered(String id ,String json) {


        userRepo.update(id , json);
    }


}