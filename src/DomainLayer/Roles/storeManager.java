package DomainLayer.Roles;

import java.util.Dictionary;
import java.util.Hashtable;

public class storeManager {

    private Dictionary<String,Boolean> permissions;
    private int storeID;

    storeManager(int StoreID){
        permissions = new Hashtable<String,Boolean>();
        this.storeID = StoreID;
    }


}
