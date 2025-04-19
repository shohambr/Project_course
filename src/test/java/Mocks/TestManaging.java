package Mocks;

import DomainLayer.Roles.Jobs.Managing;
import DomainLayer.Roles.Jobs.Ownership;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;

/**
 * A test implementation of Managing that can be used in tests
 * instead of mocking Managing, which might have issues with Mockito.
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
public class TestManaging extends Managing {
    private Store testStore;

    public TestManaging(RegisteredUser user, Store store, Ownership jobGiver) {
        super(store, user, jobGiver, new boolean[]{true, true, true, true, true});
        this.testStore = store;
    }

    @Override
    public Store getStore() {
        return testStore;
    }
}