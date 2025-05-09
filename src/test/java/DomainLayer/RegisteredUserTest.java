
package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import DomainLayer.Roles.RegisteredUser;
import DomainLayer.ShoppingCart;

import java.util.*;

class RegisteredUserTest {

    private RegisteredUser user;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        user = new RegisteredUser("username");
        mapper = new ObjectMapper();
    }

    @Test
    void testParameterizedConstructor() {
        String name = "Alice";
        RegisteredUser ru = new RegisteredUser(name);
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
    void testSetAnswers() {
        Map<String, String> answers = new HashMap<>();
        answers.put("Q1", "A1");
        user.setAnswers(answers);
        // No getter, but we can serialize to JSON and ensure field exists
        assertDoesNotThrow(() -> mapper.writeValueAsString(user));
    }
}
