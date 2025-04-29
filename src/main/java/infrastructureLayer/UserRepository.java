package infrastructureLayer;
import DomainLayer.IUserRepository;
import DomainLayer.Store;
import DomainLayer.User;

import java.util.HashMap;


public class UserRepository implements IUserRepository {
    //entry in the hashmap is of the form <username , (pass;json)>
    HashMap<String , String> rep = new HashMap<String ,String>();
    HashMap<String , String> pass = new HashMap<String ,String>();

    public static void sendNewOwnershipRequest(int newOwnerId, Store myStore) {

    }

    public String getUserPass(String username){
        return pass.get(username);
    }

    public boolean addUser(String userId,String userName , String hashedPassword , String json) {
        if(rep.containsKey(userId)){
            throw new IllegalArgumentException("User already exists");
        }
        rep.put(userId , json);
        pass.put(userName, hashedPassword);
        return true;
    }

    public boolean isUserExist(String username) {
        return pass.containsKey(username);
    }

    public boolean update(String userId, String s) {

        if(!rep.containsKey(s)){
            return false;
        }
        rep.replace(userId , s);
        return true;
    }

    public String getUser(String userId) {
        return rep.get(userId);
    }
}