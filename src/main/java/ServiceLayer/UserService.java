package ServiceLayer;
import DomainLayer.IUserRepository;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    //Should hold IUserRepository (that practically will always be UserRepository)
    private BCrypt passwordEncoder;
    private TokenService tokenService;
    private IUserRepository userRepository;

    public UserService(IUserRepository repository, TokenService tokenService) {
        this.userRepository = repository;
        this.tokenService = tokenService;
    }

    public String login(String username, String password){
        String userPass = userRepository.getUserPass(username);
        if(passwordEncoder.checkpw(password , userPass)) {
            String token = tokenService.generateToken(username);
            return token;
        }
        return "username or password incorrect";
    }

    public String signUp(String username, String password){
        if(userRepository.isUserExist(username)) {
            return "username already exists";
        }

        String hashedPassword = passwordEncoder.hashpw(password ,passwordEncoder.gensalt());
        userRepository.addUser(username , hashedPassword);
        String token = tokenService.generateToken(username);
        return token;
    }
}