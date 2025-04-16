package DomainLayer.Roles.Jobs;

import DomainLayer.Store;

import java.util.HashMap;
import java.util.LinkedList;

public class Managing extends Job {
    private HashMap<String, Boolean> permissions;

    public Managing(Store store, Ownership jobOfferer, int myID, boolean[] permissions) {
        super(store,myID);
        this.permissions = new HashMap<>();
        this.permissions.put("addNewProduct", Boolean.valueOf(permissions[0]));
        this.permissions.put("changeProductQuantity", Boolean.valueOf(permissions[1]));
        this.permissions.put("changeProductPrice", Boolean.valueOf(permissions[2]));
        this.permissions.put("changeProductDescription", Boolean.valueOf(permissions[3]));
        this.permissions.put("removeProductFromInventory", Boolean.valueOf(permissions[4]));
        this.mySubordinates = new LinkedList<>();
        this.mySuperiors = new LinkedList<>();
        this.mySuperiors.add(jobOfferer);
    }

    public void addNewProduct(String name, String description, int price, int quantity) {
        if (permissions.get("addNewProduct")) {
            super.addNewProduct(name, quantity);
        } else {
            sendErrorMessage("you do not have the right permissions for this action\n");
        }
    }

    public void changeProductQuantity(int productID, int quantity) {
        if (permissions.get("changeProductQuantity")) {
            super.changeProductQuantity(productID, quantity);
        } else {
            sendErrorMessage("you do not have the right permissions for this action\n");
        }
    }

//    public void changeProductPrice(int productID, int price) {
//        if (permissions.get("changeProductPrice")) {
//            super.changeProductPrice(productID, price);
//        } else {
//            sendErrorMessage("you do not have the right permissions for this action\n");
//        }
//    }

//    public void changeProductDescription(int productID, String description) {
//        if (permissions.get("changeProductDescription")) {
//            super.changeProductDescription(productID, description);
//        } else {
//            sendErrorMessage("you do not have the right permissions for this action\n");
//        }
//    }

    public void removeProductFromInventory(String productID) {
        if (permissions.get("removeProductFromInventory")) {
            super.removeProductFromInventory(productID);
        } else {
            sendErrorMessage("you do not have the right permissions for this action\n");
        }
    }

    public void sendErrorMessage(String message) {
        super.sendErrorMessage(message);
    }

}
