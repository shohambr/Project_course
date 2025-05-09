# Why Use Test Implementations Instead of Real Implementations or Mocking

This document explains why the project uses test implementations (`TestRegisteredUser`, `TestOwnership`, `TestManaging`, and `TestStore`) instead of using the real implementations or mocking them with Mockito.

## Issues with Mocking Complex Domain Objects

The comments in the test implementation classes explicitly state that these classes are used "instead of mocking [class], which has issues with Mockito." There are several reasons why mocking these classes might be problematic:

1. **Complex Inheritance Hierarchies**: Classes like `RegisteredUser` extend `User`, and `Ownership` and `Managing` extend `Job`. Mockito can have issues with complex inheritance hierarchies.

2. **Final Methods or Classes**: If any methods or classes are marked as `final`, Mockito cannot mock them without special configuration.

3. **Static Methods**: Mockito cannot mock static methods without extensions like PowerMock.

4. **Complex Constructors**: Classes like `RegisteredUser` have constructors that perform complex operations like JSON deserialization, which can be difficult to mock.

5. **Internal State**: These domain classes maintain complex internal state that would need to be carefully set up for each test.

## Issues with Using Real Implementations

Using the real implementations in tests would also be problematic:

1. **External Dependencies**: The real implementations have dependencies on external services like `ProductService` and `PaymentService`.

2. **Complex Setup**: Setting up the real implementations would require initializing all their dependencies and internal state.

3. **Side Effects**: The real implementations might have side effects like modifying databases or sending notifications.

4. **Slow Tests**: Using real implementations with all their dependencies would make tests slower.

## Advantages of Test Implementations

The test implementations provide several advantages:

1. **Simplicity**: They are much simpler than the real implementations, focusing only on the behavior needed for tests.

2. **Control**: They allow precise control over the behavior of the objects in tests.

3. **Isolation**: They isolate tests from external dependencies and side effects.

4. **Speed**: They make tests faster by avoiding complex initialization and external calls.

5. **Testability**: They make it easier to test specific scenarios by providing methods like `setTestJobs()` in `TestRegisteredUser`.

## Example: TestRegisteredUser vs. RegisteredUser

- `RegisteredUser` has complex JSON serialization/deserialization logic and methods for handling ownership and managing requests.
- `TestRegisteredUser` simply overrides `getID()` and `getJobs()` to return test values, and provides a `setTestJobs()` method for test setup.

## Example: TestOwnership vs. Ownership

- `Ownership` creates a new Store in its constructor and has complex methods for finding subordinates and handling customer queries.
- `TestOwnership` allows setting the founder flag in the constructor and provides empty implementations for methods like `closeStore()`.

## Example: TestManaging vs. Managing

- `Managing` has a complex permissions system and methods that check permissions before performing actions.
- `TestManaging` initializes with all permissions set to true and only overrides `getStore()`.

## Example: TestStore vs. Store

- `Store` has many fields and methods for managing products, users, and store operations.
- `TestStore` only overrides `getId()` and provides empty implementations for `closeTheStore()` and `openTheStore()`.

## Conclusion

Using test implementations instead of real implementations or mocking provides a good balance between test fidelity and simplicity. It allows testing the behavior of the system without the complexity of the real implementations or the limitations of mocking.