package DomainLayer.Roles.Jobs;

import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;

import java.util.LinkedList;

public class Ownership extends Job {
    //the store founder constructor
    public Ownership(String storeName, int id) {
        super(id);
        mySubordinates = new LinkedList<>();
        mySuperiors = new LinkedList<>();
        this.myStore = new Store();
        this.appointingOwnerID = -1;

    }
    /**
     * constructor for sub-owners
     * @param store the store...
     * @param jobOfferer the job taken by the registered user
     * @param myID the id of the job giver
     */
    public Ownership(Store store, Ownership jobOfferer, int myID) {
        super(store,myID);
        this.mySubordinates = new LinkedList<>();
        this.mySuperiors = new LinkedList<>();
        this.mySuperiors.add(jobOfferer);
    }
    public void appointNewOwnerRequest(RegisteredUser newOwner){
        for (Job j: newOwner.getJobs()){
            if (j.myStore==this.myStore){
                if (j instanceof Ownership){
                    sendErrorMessage("the user is already an owner for the store");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Hi").append(newOwner.getName()).append(",\n")
                .append("I would like for you to become a co-owner for my store: \"")
                .append(this.myStore.getId()).append("\"\n")
                .append("please pleaseeee say YES!\n");
        newOwner.becomeNewOwnerRequest(sb.toString(),new Ownership(myStore,this,newOwner.getID()),this);
    }
    public void removeOwnerRequest(RegisteredUser Owner){
        for(Job j : Owner.getJobs()){
            if(this.mySubordinates.contains(j)){
                this.mySubordinates.remove(j);
                j.getFired();
            }
        }
    }
    public void resignation(){
        this.getFired();
    }
    public void appointNewManagerRequest(RegisteredUser newManager,Store myStore,boolean[] permissions){
        for (Job j: newManager.getJobs()){
            if (j.myStore==this.myStore){
                if (j instanceof Managing){
                    sendErrorMessage("the user is already a manager for the store");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Hi").append(newManager.getName()).append(",\n")
                .append("I would like for you to become a manager for my store: \"")
                .append(this.myStore.getId()).append("\"\n")
                .append("please pleaseeee say YES!\n");
        newManager.becomeNewManagerRequest(sb.toString(),new Managing(myStore, this, newManager.getID(),permissions),this);
    }
    public void changeManagerPermissions(RegisteredUser newManager,boolean[] permissions){

    }
    public void removeManagerRequest(RegisteredUser manager){
        for(Job j : manager.getJobs()){
            if(this.mySubordinates.contains(j)){
                this.mySubordinates.remove(j);
                j.getFired();
            }
        }
    }
    public void closeStore(){
        if(this.appointingOwnerID == -1){
            this.myStore.closeTheStore();
            for(Job j : mySubordinates){
                j.alertStoreClosed();
            }
            this.getFired();
        }
    }
    public void reOpenStore(){
        if(this.appointingOwnerID == -1){
            this.myStore.openTheStore();
            for(Job j : mySubordinates){
                j.alertStoreReOpened();
                j.reHire();
            }
        }
    }
    /**
     * this function is to be used by the job offerer to add his subordinate under him
     * @param acceptedJobOffer is the job that was given
     */
    public void jobOfferAccepted(Job acceptedJobOffer) {
        this.mySubordinates.add(acceptedJobOffer);
    }
    public void jobOfferDeclined(Job jobOffer) {
        //well fuck you. I don't even like you!
    }
}