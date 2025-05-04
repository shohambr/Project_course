package ServiceLayer;

import DomainLayer.IShipping;
import DomainLayer.Product;
import DomainLayer.Roles.Guest;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;
import DomainLayer.User;
import Mocks.MockPayment;
import infrastructureLayer.ProxyShipping;
import Mocks.MockShipping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShippingServiceTest {

    private ShippingService shippingService;
    private User user;
    private Store store;

    @BeforeEach
    void setUp() {
        store = new Store();
        Product product = new Product("1", store.getId(), "bgdfbf", "bdfgbfgds", 321, 3, 1.0, "1223r");
        store.addNewProduct(product.getId(), 3);
        user = new RegisteredUser();
        user.addProduct(store, product);
        IShipping mockShipping = new MockShipping();
        shippingService = new ShippingService(mockShipping);
    }
    @Test
    public void testProcessShipping_Successful() {
        boolean response = shippingService.processShipping(user, "Israel", "Be'er Sheva", "Even Gvirol", "12");
        assertTrue(response);
    }

    @Test
    public void testProcessShipping_EmptyState_Failure() {
        boolean response = shippingService.processShipping(user, "", "Be'er Sheva", "Even Gvirol", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_EmptyCity_Failure() {
        boolean response = shippingService.processShipping(user, "Israel", "", "Even Gvirol", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_EmptyStreet_Failure() {
        boolean response = shippingService.processShipping(user, "Israel", "Be'er Sheva", "", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_InvalidHomeNumber_Failure() {
        boolean response = shippingService.processShipping(user, "Israel", "Be'er Sheva", "Even Gvirol", "vjmikod");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_EmptyHomeNumber_Failure() {
        boolean response = shippingService.processShipping(user, "Israel", "Be'er Sheva", "Even Gvirol", "");
        assertFalse(response);
    }




}
