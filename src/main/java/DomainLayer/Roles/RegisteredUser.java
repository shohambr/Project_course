package DomainLayer.Roles;

import java.util.*;
import DomainLayer.Roles.Jobs.Job;
import DomainLayer.Roles.Jobs.Managing;
import DomainLayer.Roles.Jobs.Ownership;
import DomainLayer.ShoppingCart;
import DomainLayer.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegisteredUser extends User {

    ObjectMapper mapper = new ObjectMapper();
    private List<Job> jobs;
    private Map<String, String> answers = new HashMap<>();
    private String name;


    public RegisteredUser(String name) {
        super();
         //needed for Jackson
        this.name = name;
    }
    public RegisteredUser() {
        super();
        this.jobs = new ArrayList<>();
        this.name = "";
    }

    public String getUsername() {
        return this.name;
    }


    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public List<Job> getJobs() {
        return jobs;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public boolean receivedOwnershipRequest(String request) {
        //some logic for how to show the user that he received an ownership request
        return returnOwnershipRequestAnswer();
    }
    public boolean returnOwnershipRequestAnswer() {
        return false;
    }

    public boolean receivedManagingRequest(String request) {
        //some logic for how to show the user that he received a managing request
        return returnManagingRequestAnswer();
    }
    private boolean returnManagingRequestAnswer() {
        return false;
    }

    public void acceptQueryResponse(String s) {

    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}