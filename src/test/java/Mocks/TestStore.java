package Mocks;

import DomainLayer.Store;

/**
 * A test implementation of Store that can be used in tests
 * instead of mocking Store, which has issues with Mockito.
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
public class TestStore extends Store {
    private String testId;

    public TestStore(String id) {
        super();
        this.testId = id;
    }

    @Override
    public String getId() {
        return testId;
    }

    @Override
    public void closeTheStore() {
        // Test implementation
    }

    @Override
    public void openTheStore() {
        // Test implementation
    }
}