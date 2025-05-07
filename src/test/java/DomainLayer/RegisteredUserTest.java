
package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Roles.Jobs.Job;
import DomainLayer.ShoppingCart;

import java.util.*;

class RegisteredUserTest {

    private RegisteredUser user;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        user = new RegisteredUser();
        mapper = new ObjectMapper();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(user.getJobs(), "Jobs list should not be null");
        assertTrue(user.getJobs().isEmpty(), "Jobs list should start empty");
        assertEquals("", user.getName(), "Default name should be empty");
        assertEquals(user.getName(), user.getUsername(), "Username should match name");
        ShoppingCart cart = user.getShoppingCart();
        assertNotNull(cart, "ShoppingCart should not be null");
    }

    @Test
    void testParameterizedConstructor() {
        List<Job> jobs = new ArrayList<>();
        String name = "Alice";
        RegisteredUser ru = new RegisteredUser(jobs, name);
        assertSame(jobs, ru.getJobs());
        assertEquals(name, ru.getName());
        assertEquals(name, ru.getUsername());
    }

    @Test
    void testSetName() {
        String newName = "Bob";
        user.setName(newName);
        assertEquals(newName, user.getName());
        assertEquals(newName, user.getUsername());
    }

    @Test
    void testReceivedOwnershipRequest_DefaultFalse() {
        assertFalse(user.receivedOwnershipRequest("request"));
    }

    @Test
    void testReceivedManagingRequest_DefaultFalse() {
        assertFalse(user.receivedManagingRequest("request"));
    }

    @Test
    void testSetAnswers() {
        Map<String, String> answers = new HashMap<>();
        answers.put("Q1", "A1");
        user.setAnswers(answers);
        // No getter, but we can serialize to JSON and ensure field exists
        assertDoesNotThrow(() -> mapper.writeValueAsString(user));
    }

    @Test
    void testJsonConstructorCopiesFields() throws Exception {
        user.setName("Carol");
        String json = mapper.writeValueAsString(user);
        RegisteredUser reconstructed = new RegisteredUser(json);
        assertEquals(user.getName(), reconstructed.getName(), "Name should persist through JSON constructor");
        assertEquals(user.getJobs().size(), reconstructed.getJobs().size(), "Jobs list size should persist through JSON constructor");
        assertNotNull(reconstructed.getShoppingCart(), "ShoppingCart should not be null after JSON constructor");
        assertEquals(
            user.getShoppingCart().getShoppingBags().size(),
            reconstructed.getShoppingCart().getShoppingBags().size(),
            "ShoppingBags size should persist through JSON constructor"
        );
    }
}
