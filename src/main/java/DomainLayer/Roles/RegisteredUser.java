package DomainLayer.Roles;

import java.util.*;


import DomainLayer.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class RegisteredUser extends User {

    ObjectMapper mapper = new ObjectMapper();

    private HashMap<Integer, HashMap<String,Boolean>> jobs;

    public RegisteredUser(String json) {
        try {
            RegisteredUser temp = mapper.readValue(json , RegisteredUser.class);
            this.jobs = temp.jobs;
            this.id = temp.id;
            this.shoppingCart = temp.shoppingCart;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.jobs = new HashMap<>();

    }
    public void logout()  {
        try{
            userService.logoutRegistered(this.myToken, mapper.writeValueAsString(this));
            myToken=null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public String register(String u , String p){
        throw new UnsupportedOperationException("allready registered.");
    }
    public HashMap<Integer, HashMap<String, Boolean>> getJobs() {
        return jobs;
    }
    public void setJobs(HashMap<Integer, HashMap<String, Boolean>> jobs) {
        this.jobs = jobs;
    }

    public void createStore(String storeName){
        //to implement
    }

    public int getID() {
        return this.id;
    }

    public void ownershipRequest(String request) {
        //to implement
    }
}
