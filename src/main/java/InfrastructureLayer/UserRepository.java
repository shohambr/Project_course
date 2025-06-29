package InfrastructureLayer;
import DomainLayer.IUserRepository;
import DomainLayer.Store;
import DomainLayer.User;
import DomainLayer.Roles.RegisteredUser;
import io.micrometer.observation.Observation.Event;
import ServiceLayer.EventLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


@Repository
public class UserRepository implements IUserRepository {
    //entry in the hashmap is of the form <username , (pass;json)>
    HashMap<String , String> rep = new HashMap<String ,String>();
    HashMap<String , String> pass = new HashMap<String ,String>();
    public final ObjectMapper mapper = new ObjectMapper();


    public String getUserPass(String username){
        return pass.get(username);
    }

    public boolean addUser(String username, String hashedPassword , String json) {
        if(rep.containsKey(username)){
            throw new IllegalArgumentException("User already exists");
        }
        rep.put(username , json);
        pass.put(username, hashedPassword);
        return true;
    }

    public boolean isUserExist(String username) {
        return rep.containsKey(username);
    }

    public boolean existsById(String username) {
        return rep.containsKey(username);
    }

    public RegisteredUser getById(String username) {
        String json = rep.get(username);
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, RegisteredUser.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public boolean update(String name, String s) {

        if(!rep.containsKey(s)){
            return false;
        }
        else{
            rep.put(s, name);
            return true;
        }


    }

    public void update(RegisteredUser user) {
        try {
            String json = mapper.writeValueAsString(user);
            rep.put(user.getUsername(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    public String getUser(String username) {
        return rep.get(username);
    }
}