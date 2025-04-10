package ServiceLayer;
import infrastructureLayer.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    //Should hold IUserRepository (that practically will always be UserRepository)
    private BCrypt passwordEncoder;
    private TokenService tokenService;

    public String login(String username , String password){
        String userPass = UserRepository.findPassByName(username);
        if(passwordEncoder.checkpw(password , userPass)) {
            String token = tokenService.generateToken(username);
            return token;
        }
        return "username or password incorrect";
    }

    public String signUp(String username , String password){
        if(UserRepository.findall(username)!=null) {
            return "username already exists";
        }

        String hashedPassword = passwordEncoder.hashpw(password ,passwordEncoder.gensalt());
        UserRepository.insert(username , hashedPassword);
        String token = tokenService.generateToken(username);
        return token;
    }
}