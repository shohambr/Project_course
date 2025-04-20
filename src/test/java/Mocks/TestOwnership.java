package Mocks;

import DomainLayer.Roles.Jobs.Ownership;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;

/**
 * A test implementation of Ownership that can be used in tests
 * instead of mocking Ownership, which might have issues with Mockito.
 * 
 * This class is part of a group of test implementation classes:
 * - TestRegisteredUser
 * - TestOwnership
 * - TestManaging
 * - TestStore
 * 
 * See README.md in this directory for more details on why these
 * test implementations are used instead of mocking or real implementations.
 */
public class TestOwnership extends Ownership {
    private boolean isFounderFlag;
    private Store testStore;

    public TestOwnership(RegisteredUser user, Store store, boolean isFounder) {
        super(user, store);
        this.isFounderFlag = isFounder;
        this.testStore = store;
    }

    @Override
    public boolean isFounder() {
        return isFounderFlag;
    }

    @Override
    public Store getStore() {
        return testStore;
    }

    @Override
    public void closeStore() {
        // Test implementation
    }

    @Override
    public void reOpenStore() {
        // Test implementation
    }
}