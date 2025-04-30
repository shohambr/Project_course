package ServiceLayer;

import DomainLayer.Product;
import DomainLayer.Roles.Guest;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;
import DomainLayer.User;
import DomainLayer.domainServices.PaymentConnectivity;
import infrastructureLayer.ProxyPayment;
import Mocks.MockPayment;
import ServiceLayer.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentService paymentService;
    private User user;
    private Store store;
    @BeforeEach
    void setUp() {
        store = new Store();
        Product product = new Product("1", store.getId(), "bgdfbf", "bdfgbfgds", 321, 3, 1.0, "1223r");
        store.addNewProduct(product, 3);
        MockPayment mockPayment = new MockPayment();
        paymentService = new PaymentService(mockPayment);
        user = new RegisteredUser();
        user.addProduct(store, product);
    }

    @Test
    public void testProcessPayment_Successful() {
        boolean response = paymentService.processPayment(user,"csda", "5555555555554444", "10/26", "395");
        assertTrue(response);
    }

    @Test
    public void testProcessPayment_BadCreditCardNumber_Failure() {
        boolean response = paymentService.processPayment(user, "csda","5355555555554444", "10/26", "395");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_EmptyCreditCardNumber_Failure() {
        boolean response = paymentService.processPayment(user, "csda", "", "10/26", "395");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_BadlyWrittenExpirationDate_Failure() {
        boolean response = paymentService.processPayment(user, "csda","5555555555554444", "10'26", "395");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_InvalidExpirationDate_Failure() {
        boolean response = paymentService.processPayment(user, "csda","5555555555554444", "fewdki", "395");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_EmptyExpirationDate_Failure() {
        boolean response = paymentService.processPayment(user, "csda", "5555555555554444", "", "395");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_InvalidBackNumber_Failure() {
        boolean response = paymentService.processPayment(user, "csda","5555555555554444", "10/26", "kfjeowia0");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_ExpiredCreditCard_Failure() {
        boolean response = paymentService.processPayment(user, "csda", "5555555555554444", "10/24", "395");
        assertFalse(response);
    }

    @Test
    public void testProcessPayment_EmptyBackNumber_Failure() {
        boolean response = paymentService.processPayment(user,"csda", "5555555555554444", "10/26", "");
        assertFalse(response);
    }



}
