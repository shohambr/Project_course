package DomainLayer.Roles.Jobs;

import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;

import java.util.*;

public abstract class Job {
    private boolean founder;
    protected RegisteredUser registeredUser;
    private boolean currentlyWorking;
    protected List<Job> mySubordinates;
    protected List<Job> mySuperiors;
    protected Store myStore;
    //would be implemented by a queue in the next sprint
    //protected magic notification;

    public Job(RegisteredUser founder) {
        registeredUser = founder;
        currentlyWorking = (Boolean) true;
        this.founder = true;
        mySuperiors = new LinkedList<Job>();
    }
    public Job(Store myStore, RegisteredUser me,Ownership jobGiver) {
        this.founder = false;
        this.registeredUser = me;
        this.myStore = myStore;
        currentlyWorking = (Boolean) true;
        mySuperiors = new LinkedList<Job>();
        mySuperiors.add(jobGiver);
        me.getJobs().add(this);
    }
    public boolean isFounder() {
        return founder;
    }
    public String getMyID(){
        return registeredUser.getID();
    }
    public Store getStore(){
        return myStore;
    }
    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }
    public void sendErrorMessage(String message) {
        // notification.message(message);
    }
    //4.1
    public void addNewProduct(String name, int quantity) {
        if (isCurrentlyWorking()) {
            myStore.addNewProduct(name, quantity);
        } else {
            sendErrorMessage("is not currently employed in this store\n");
        }
    }
    public void changeProductQuantity(String productID, int quantity) {
        if (isCurrentlyWorking()) {
            myStore.changeProductQuantity(productID, quantity);
        } else {
            sendErrorMessage("is not currently employed in this store\n");
        }
    }
//    public void changeProductPrice(String productID, int price){
//        if(isCurrentlyWorking()){
//            myStore.changeProductPrice(productID,price);
//        } else {
//            sendErrorMessage("is not currently employed in this store\n");
//        }
//     }
//    public void changeProductDescription(String productID, String description){
//        if(isCurrentlyWorking()){
//            myStore.changeProductDescription(productID,description);
//        } else {
//            sendErrorMessage("is not currently employed in this store\n");
//        }
//     }
    public void removeProductFromInventory(String productID) {
        if (isCurrentlyWorking()) {
            myStore.removeProduct(productID);
        } else {
            sendErrorMessage("is not currently employed in this store\n");
        }
    }
    protected void alertStoreClosed() {
        StringBuilder sb = new StringBuilder();
        sb.append("Store: ").append(myStore.getId()).append(". closed\n");
        sendErrorMessage(sb.toString());
    }
    protected void alertStoreReOpened() {
        StringBuilder sb = new StringBuilder();
        sb.append("Store: ").append(myStore.getId()).append(". reopened\n");
        sendErrorMessage(sb.toString());
    }
    protected void getFired() {
        this.currentlyWorking = false;
    }
    protected void reHire() {
        this.currentlyWorking = true;
    }
    protected boolean isCurrentlyWorking() {
        return currentlyWorking;
    }
    public boolean findSubordinate(Job subordinateJob) {
        return false;
    }
    public void delete() {
        for (Job j : mySubordinates) {
            j.delete();
        }
        for (Job j : mySuperiors) {
            mySubordinates.remove(this);
        }
        this.getRegisteredUser().getJobs().remove(this);
    }
    public void closeStore(){
        for(Job j : mySubordinates){
            j.alertStoreClosed();
            j.closeStore();
        }
        this.getFired();
    }
    public void reOpenStore(){
        for(Job j : mySubordinates){
            j.alertStoreReOpened();
            j.reOpenStore();
        }
        this.reHire();
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (founder) sb.append("founder: \n");
        sb.append("userID: ").append(this.getMyID());
        if (!founder)sb.append("my SuperiorID: ").append(this.mySuperiors.get(0).getMyID()).append("\n");
        if(!(this instanceof Managing)) sb.append("my subordinates: \n");
        int i = 1;
        for(Job j : mySubordinates) {
            sb.append(i).append(": ").append("\n");
            sb.append(j.toString()).append("\n");
            sb.append("------------------------\n");
            i++;
        }
        return sb.toString();
    }
}