package infrastructureLayer;
import DomainLayer.IUserRepository;

import java.util.HashMap;


public class UserRepository implements IUserRepository {
    //entry in the hashmap is of the form <username , (pass;json)>
    HashMap<String , String> rep = new HashMap<String ,String>();
    HashMap<String , String> pass = new HashMap<String ,String>();

    public String getUserPass(String username){
        return pass.get(username);
    }

    public boolean addUser(String username, String hashedPassword) {
        if(rep.containsKey(username)){
            return false;
        }
        rep.put(username , "");
        pass.put(username, hashedPassword);
        return true;
    }

    public boolean isUserExist(String username) {
        return rep.containsKey(username);
    }

    public boolean update(String name, String s) {

        if(!rep.containsKey(s)){
            return false;
        }
        rep.replace(name , s);
        return true;
    }


    public String getUser(String username) {
        return rep.get(username);
    }
}