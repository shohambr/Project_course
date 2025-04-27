package ServiceLayer;

import infrastructureLayer.ProxyShipping;
import Mocks.MockShipping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShippingServiceTest {

    private ShippingService shippingService;

    @BeforeEach
    void setUp() {
        MockShipping mockShipping = new MockShipping();
        ProxyShipping proxyShipping = new ProxyShipping(mockShipping);
        shippingService = new ShippingService(proxyShipping);
    }

    @Test
    public void testProcessShipping_Successful() {
        boolean response = shippingService.processShipping("Israel", "Be'er Sheva", "Even Gvirol", "12");
        assertTrue(response);
    }

    @Test
    public void testProcessShipping_EmptyState_Failure() {
        boolean response = shippingService.processShipping("", "Be'er Sheva", "Even Gvirol", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_EmptyCity_Failure() {
        boolean response = shippingService.processShipping("Israel", "", "Even Gvirol", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_EmptyStreet_Failure() {
        boolean response = shippingService.processShipping("Israel", "Be'er Sheva", "", "12");
        assertFalse(response);
    }

    @Test
    public void testProcessShipping_InvalidHomeNumber_Failure() {
        boolean response = shippingService.processShipping("Israel", "Be'er Sheva", "Even Gvirol", "vjmikod");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_EmptyHomeNumber_Failure() {
        boolean response = shippingService.processShipping("Israel", "Be'er Sheva", "Even Gvirol", "");
        assertFalse(response);
    }




}
