// package DomainLayer.Roles;

// import DomainLayer.Store;

// import java.util.LinkedList;

// public class ownership {
//     private int appointingOwnerID;
//     private Store myStore;
//     private LinkedList<ownership> ownerships;


//     public ownership(Store store, int appointingOwnerID) {
//         this.appointingOwnerID = appointingOwnerID;
//         this.myStore = store;
//         this.ownerships = new LinkedList<>();
//     }
//     public void removeProductFromInventory(int productID){
//         myStore.removeProductFromInventory(productID);
//     }
//     public void addNewProduct(String name, String description, int price, int quantity){
//         myStore.createProduct(name,description,price,quantity);
//     }
//     public void changeProductQuantity(int productID, int quantity){
//         myStore.changeProductInventory(productID,quantity);
//     }
//     public void changeProductPrice(int productID, int price){
//         myStore.changeProductPrice(productID,price);
//     }
//     public void changeProductDescription(int productID, String description){
//         myStore.changeProductDescription(productID,description);
//     }
//     public int sendAppointingNewOwnerRequest(RegisteredUser newOwner){
//         int newOwnerID = newOwner.getID();
//         String requestMassage = "hello I would like for you to become a co-owner in my store: "+myStore.getName()+"\n";
//         newOwner.ownershipRequest(requestMassage);
//     }
//     public

// }