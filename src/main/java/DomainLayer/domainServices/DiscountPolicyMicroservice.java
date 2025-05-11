package DomainLayer.DomainServices;

import DomainLayer.*;
import DomainLayer.Roles.RegisteredUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DiscountPolicyMicroservice {

    private IStoreRepository storeRepository;
    private IUserRepository userRepository;
    private ObjectMapper mapper = new ObjectMapper();


    private Store getStoreById(String storeId) {
        if (storeRepository == null) {
            return null;
        }
        try {
            Store store = mapper.readValue(storeRepository.getStore(storeId), Store.class);
            if (storeRepository.getStore(storeId) == null) {
                throw new IllegalArgumentException("Store does not exist");
            }
            return store;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    private RegisteredUser getUserById(String userId) {
        if (userRepository == null) {
            return null;
        }
        try {
            RegisteredUser user = mapper.readValue(userRepository.getUser(userId), RegisteredUser.class);
            if (userRepository.getUser(userId) == null) {
                throw new IllegalArgumentException("User does not exist");
            }
            return user;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean checkPermission(String userId, String storeId, String permissionType) {
        // Get the store
        Store store = getStoreById(storeId);
        if (store == null) {
            return false;
        }

        // Owners have all permissions
        if (store.userIsOwner(userId)) {
            return true;
        }

        // Check if manager has specific permission
        if (store.userIsManager(userId)) {
            return store.userHasPermissions(userId, permissionType);
        }

        return false;
    }

    public DiscountPolicyMicroservice(IStoreRepository storeRepository, IUserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }


    public boolean removeDiscountFromDiscountPolicy(String ownerId, String storeId, String discountId) {
        if (checkPermission(ownerId, storeId, ManagerPermissions.PERM_UPDATE_POLICY)) {
            return getStoreById(storeId).removeDiscount(discountId);
        }
        return false;
    }

    public boolean addDiscountToDiscountPolicy(String ownerId, String storeId, String discountId,
                                        String Id,
                                        float level,
                                        float logicComposition,
                                        float numericalComposition,
                                        List<String> discountsId,
                                        float percentDiscount,
                                        String discounted,
                                        float conditional,
                                        float limiter,
                                        String conditionalDiscounted) {


        if(checkPermission(ownerId, storeId, ManagerPermissions.PERM_UPDATE_POLICY)){
            return getStoreById(storeId).addDiscount(Id, level, logicComposition, numericalComposition, discountsId, percentDiscount, discounted, conditional, limiter, conditionalDiscounted);
        }
        return false;
    }

    public boolean removeDiscountPolicy(String ownerId, String storeId) {
        if(checkPermission(ownerId, storeId, ManagerPermissions.PERM_UPDATE_POLICY)) {
            Store store = getStoreById(storeId);
            DiscountPolicy discountPolicy = new DiscountPolicy();
            store.setDiscountPolicy(discountPolicy);
        }

        return false;
    }
}
