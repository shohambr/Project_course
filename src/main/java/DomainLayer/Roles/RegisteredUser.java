package DomainLayer.Roles;

import jakarta.persistence.*;
import java.util.*;

import DomainLayer.ShoppingCart;
import DomainLayer.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "registered_users")
public class RegisteredUser extends User {

    @Transient
    private ObjectMapper mapper = new ObjectMapper();

    @ElementCollection
    @CollectionTable(name = "user_answers", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "question")
    @Column(name = "answer")
    private Map<String, String> answers = new HashMap<>();

    @Column(name = "name", nullable = false)
    private String name;

    // New user profile fields
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "location", length = 100)
    private String location;

    @ElementCollection
    @CollectionTable(name = "owned_stores", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "store_id")
    private LinkedList<String> ownedStores = new LinkedList<>();

    @ElementCollection
    @CollectionTable(name = "managed_stores", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "store_id")
    private LinkedList<String> managedStores = new LinkedList<>();

    public RegisteredUser(String username) {
        super();
        this.name = username;
    }
    public RegisteredUser(){
        super();
    }

    public String getUsername() {
        return this.name;
    }


    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }


    public RegisteredUser register(String u , String p){
        throw new UnsupportedOperationException("allready registered.");
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public void addOwnedStore(String storeId) {
        ownedStores.add(storeId);
    }
    public void addManagedStore(String storeId) {
        managedStores.add(storeId);
    }
    public LinkedList<String> getOwnedStores() {
        return ownedStores;
    }
    public LinkedList<String> getManagedStores() {
        return managedStores;
    }

    public void removeStore(String storeId) {
        this.ownedStores.remove(storeId);
        this.managedStores.remove(storeId);
    }
    public void acceptQueryResponse(String s) {

    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    // New getters and setters for profile fields
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}