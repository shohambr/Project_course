package DomainLayer.Roles.Jobs;

import DomainLayer.Store;
import infrastructureLayer.UserRepository;

import java.util.LinkedList;

public class Ownership extends Job {
    protected LinkedList<Job> mySubordinates;

    public Ownership(String storeName) {
        super();
        mySubordinates = new LinkedList<>();
        this.myStore = new Store(storeName);
        this.appointingOwnerID = -1;
    }
    public Ownership(Store store, int appointingOwnerID) {
        super(store, appointingOwnerID);
        this.mySubordinates = new LinkedList<>();
    }


    public void appointNewOwnerRequest(int newOwnerId,Store myStore){
        UserRepository.sendNewOwnershipRequest(newOwnerId,myStore);
    }

}