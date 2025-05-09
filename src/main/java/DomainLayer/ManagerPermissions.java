package DomainLayer;

import java.util.Map;
import java.util.HashMap;

public class ManagerPermissions {
    public static final String PERM_MANAGE_INVENTORY = "manageInventory";
    public static final String PERM_MANAGE_STAFF = "manageStaff";
    public static final String PERM_VIEW_STORE = "viewStore";
    public static final String PERM_UPDATE_POLICY = "updatePolicy";
    public static final String PERM_ADD_PRODUCT = "addProduct";
    public static final String PERM_REMOVE_PRODUCT = "removeProduct";
    public static final String PERM_UPDATE_PRODUCT = "updateProduct";
    private Map<String, Boolean> permissions;
    public ManagerPermissions(boolean[] perm){
        this.permissions = new HashMap<>();
        this.permissions.put(PERM_MANAGE_INVENTORY, perm[0]);
        this.permissions.put(PERM_MANAGE_STAFF, perm[1]);
        this.permissions.put(PERM_VIEW_STORE, perm[2]);
        this.permissions.put(PERM_UPDATE_POLICY, perm[3]);
        this.permissions.put(PERM_ADD_PRODUCT, perm[4]);
        this.permissions.put(PERM_REMOVE_PRODUCT, perm[5]);
        this.permissions.put(PERM_UPDATE_PRODUCT, perm[6]);
    }
    public Map<String, Boolean> getPermissions() {
        return permissions;
    }
    public boolean getPermission(String permission) {
        return permissions.getOrDefault(permission, false);
    }

    public void setPermissions(boolean[] permissions) {
        this.permissions.put(PERM_MANAGE_INVENTORY, permissions[0]);
        this.permissions.put(PERM_MANAGE_STAFF, permissions[1]);
        this.permissions.put(PERM_VIEW_STORE, permissions[2]);
        this.permissions.put(PERM_UPDATE_POLICY, permissions[3]);
        this.permissions.put(PERM_ADD_PRODUCT, permissions[4]);
        this.permissions.put(PERM_REMOVE_PRODUCT, permissions[5]);
        this.permissions.put(PERM_UPDATE_PRODUCT, permissions[6]);
    }
}
