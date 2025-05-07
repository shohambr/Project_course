package infrastructureLayer;
import DomainLayer.IUserRepository;
import DomainLayer.Store;
import DomainLayer.User;
import DomainLayer.Roles.RegisteredUser;
import io.micrometer.observation.Observation.Event;
import ServiceLayer.EventLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;




public class UserRepository implements IUserRepository {
    //entry in the hashmap is of the form <username , (pass;json)>
    HashMap<String , String> rep = new HashMap<String ,String>();
    HashMap<String , String> pass = new HashMap<String ,String>();
    public final ObjectMapper mapper = new ObjectMapper();

    public static void sendNewOwnershipRequest(int newOwnerId, Store myStore) {

    }

    public String getUserPass(String username){
        return pass.get(username);
    }

    public boolean addUser(String username , String hashedPassword , String json) {
        if(rep.containsKey(username) || pass.containsKey(username)){
            throw new IllegalArgumentException("User already exists");
        }
        rep.put(username , json);
        pass.put(username, hashedPassword);
        return true;
    }

    public boolean isUserExist(String username) {
        return pass.containsKey(username);
    }

    public boolean update(String username, String s) {
        if(!rep.containsKey(username)){
            EventLogger.logEvent(username, "User not found");
            return false;
        }
        rep.put(username , s);
        return true;
    }

    public String getUser(String username) {
        return rep.get(username);
    }
}