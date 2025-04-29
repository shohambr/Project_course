package ServiceLayer;

import DomainLayer.Roles.Guest;
import DomainLayer.Store;
import DomainLayer.User;
import infrastructureLayer.ProxyShipping;
import Mocks.MockShipping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShippingServiceTest {

    private ShippingService shippingService;
    private User user;
    private List<Store> stores;

    @BeforeEach
    void setUp() {
        MockShipping mockShipping = new MockShipping();
        shippingService = new ShippingService(mockShipping);
        stores = new ArrayList<Store>();
        stores.add(new Store());
        user = new Guest();
    }

    @Test
    public void testProcessShipping_Successful() {
        boolean response = shippingService.processShipping(user, stores, "Israel", "Be'er Sheva", "Even Gvirol", "12");
        assertTrue(response);
    }

    @Test
    public void testProcessShipping_EmptyState_Failure() {
        boolean response = shippingService.processShipping(user, stores, "", "Be'er Sheva", "Even Gvirol", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_EmptyCity_Failure() {
        boolean response = shippingService.processShipping(user, stores, "Israel", "", "Even Gvirol", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_EmptyStreet_Failure() {
        boolean response = shippingService.processShipping(user, stores, "Israel", "Be'er Sheva", "", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_InvalidHomeNumber_Failure() {
        boolean response = shippingService.processShipping(user, stores, "Israel", "Be'er Sheva", "Even Gvirol", "vjmikod");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_EmptyHomeNumber_Failure() {
        boolean response = shippingService.processShipping(user, stores, "Israel", "Be'er Sheva", "Even Gvirol", "");
        assertFalse(response);
    }




}
