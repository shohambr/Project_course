package DomainLayer.Roles.Jobs;

import DomainLayer.Store;

import javax.management.Notification;
import java.util.LinkedList;

public class Job {
//     protected store closed notification
    protected int appointingOwnerID;
    protected Store myStore;
    public Job(){

    }
    public Job(Store myStore, int appointingOwnerID) {
        this.appointingOwnerID = appointingOwnerID;
        this.myStore = myStore;
    }
     //4.1
    public void addNewProduct(String name, String description, int price, int quantity){
         myStore.createProduct(name,description,price,quantity);
     }
    public void changeProductQuantity(int productID, int quantity){
         myStore.changeProductInventory(productID,quantity);
     }
    public void changeProductPrice(int productID, int price){
         myStore.changeProductPrice(productID,price);
     }
    public void changeProductDescription(int productID, String description){
         myStore.changeProductDescription(productID,description);
     }
    public void removeProductFromInventory(int productID){
         myStore.removeProductFromInventory(productID);
     }


 }