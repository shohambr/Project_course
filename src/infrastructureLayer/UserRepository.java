package infrastructureLayer;
import org.mindrot.jbcrypt.BCrypt;
import java.util.*;
import java.util.HashMap;
import

public interface UserRepository extends MongoRepository<String,String> {

    HashMap<String , String> rep = new HashMap<String ,String>;



    public static String findPassByName(String username){
        return rep.get(username);
    }

    static String insert(String username, String hashedPassword) {
    }


}