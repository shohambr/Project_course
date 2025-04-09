package DomainLayer.Roles;
import DomainLayer.User;

import java.util.*;


public class RegisteredUser extends User {
    /*the following hashmap a hash that contains all the storeIDs that the user has a "job" in
    AKA store owner or store manager. for each store it holds the string permmision and the boolean for if it has that */
    private HashMap<Integer, HashMap<String,Boolean>> jobs;


    public RegisteredUser(int id){

        jobs = new HashMap<Integer,HashMap<String,Boolean>>();
    }
    public String logout(String u , String p){
        //in the future should throw an error message
        return null;
    }
    public String register(String u , String p){
        //in the future should throw an error message
        return null;
    }

    public HashMap<Integer, HashMap<String, Boolean>> getJobs() {
        return jobs;
    }

    public void setJobs(HashMap<Integer, HashMap<String, Boolean>> jobs) {
        this.jobs = jobs;
    }
    public void createStore(String storeName){

    }
}
