package DomainLayer.Roles.Jobs;
import DomainLayer.Store;
import java.util.*;

public abstract class Job {
    protected int myID;
    private Boolean currentlyWorking;
    protected List<Job> mySubordinates;
    protected List<Job> mySuperiors;
    protected int appointingOwnerID;
    protected Store myStore;
    protected magic notification;

    public Job(int id){
        myID = id;
        currentlyWorking = true;

    }
    public Job(Store myStore, int appointingOwnerID) {
        this.appointingOwnerID = appointingOwnerID;
        this.myStore = myStore;
        currentlyWorking = true;
    }
    public void sendErrorMessage(String message){
        notification.message(message);
    }
     //4.1
    public void addNewProduct(String name, String description, int price, int quantity) {
        if (isCurrentlyWorking()) {
            myStore.createProduct(name, description, price, quantity);
        } else {
            sendErrorMessage("is not currently employed in this store\n");
        }
    }
    public void changeProductQuantity(int productID, int quantity){
        if(isCurrentlyWorking()){
            myStore.changeProductInventory(productID,quantity);
        } else {
            sendErrorMessage("is not currently employed in this store\n");
        }
     }
    public void changeProductPrice(int productID, int price){
        if(isCurrentlyWorking()){
            myStore.changeProductPrice(productID,price);
        } else {
            sendErrorMessage("is not currently employed in this store\n");
        }
     }
    public void changeProductDescription(int productID, String description){
        if(isCurrentlyWorking()){
            myStore.changeProductDescription(productID,description);
        } else {
            sendErrorMessage("is not currently employed in this store\n");
        }
     }
    public void removeProductFromInventory(int productID){
        if(isCurrentlyWorking()){
            myStore.removeProductFromInventory(productID);
        } else {
            sendErrorMessage("is not currently employed in this store\n");
        }
     }
    protected void alertStoreClosed() {
        StringBuilder sb = new StringBuilder();
        sb.append("Store").append(myStore.getName()).append("closed\n");
        sendErrorMessage(sb.toString());
    }
    protected void alertStoreReOpened() {
        StringBuilder sb = new StringBuilder();
        sb.append("Store").append(myStore.getName()).append("reopened\n");
        sendErrorMessage(sb.toString());
    }
    protected void getFired() {
        this.currentlyWorking = false;
        for(Job j : mySubordinates){
            j.getFired();
        }
    }
    protected void reHire() {
        this.currentlyWorking = true;
        for(Job j : mySubordinates){
            j.reHire();
        }

    }
    protected boolean isCurrentlyWorking() {
        return currentlyWorking;
    }
}