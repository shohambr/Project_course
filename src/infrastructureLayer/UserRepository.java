package infrastructureLayer;
import DomainLayer.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import java.util.*;
import java.util.HashMap;


public class UserRepository implements IUserRepository {

    HashMap<String , String> rep = new HashMap<String ,String>();

    public String getUserPass(String username){
        return rep.get(username);
    }

    public boolean addUser(String username, String hashedPassword) {
        if(rep.containsKey(username)){
            return false;
        }
        rep.put(username , hashedPassword);
        return true;
    }

    public boolean isUserExist(String username) {
        return rep.containsKey(username);
    }
}