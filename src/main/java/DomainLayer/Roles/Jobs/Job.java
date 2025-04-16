package DomainLayer.Roles.Jobs;

import DomainLayer.Store;

import java.util.*;

public abstract class Job {
    protected int myID;
    private boolean currentlyWorking;
    protected List<Job> mySubordinates;
    protected List<Job> mySuperiors;
    protected int appointingOwnerID;
    protected Store myStore;
    //would be implemented by a queue in the next sprint
    //protected magic notification;

    public Job(int id) {
        myID = id;
        currentlyWorking = (Boolean) true;

    }

    public Job(Store myStore, int appointingOwnerID) {
        this.appointingOwnerID = appointingOwnerID;
        this.myStore = myStore;
        currentlyWorking = (Boolean) true;
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
        sb.append("Store").append(myStore.getId()).append("closed\n");
        sendErrorMessage(sb.toString());
    }

    protected void alertStoreReOpened() {
        StringBuilder sb = new StringBuilder();
        sb.append("Store").append(myStore.getId()).append("reopened\n");
        sendErrorMessage(sb.toString());
    }

    protected void getFired() {
        this.currentlyWorking = false;
        for (Job j : mySubordinates) {
            j.getFired();
        }
    }

    protected void reHire() {
        this.currentlyWorking = true;
        for (Job j : mySubordinates) {
            j.reHire();
        }

    }

    protected boolean isCurrentlyWorking() {
        return currentlyWorking;
    }
}