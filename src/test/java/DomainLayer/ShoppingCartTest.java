package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartTest {

    private Store store;
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        store = new Store("Store");
        shoppingCart = new ShoppingCart(1);
    }

    @Test
    void addNewProductInNewStore_Successful() {
        Product product = new Product("1", "1", "bgdfbf", "bdfgbfgds", 321, 3);

        shoppingCart.addProduct(store, product);

        assertTrue(shoppingCart.getShoppingBags().size() == 1 & shoppingCart.getShoppingBags().get(0).getProducts().get(product) == 1);
    }

    @Test
    void addNewProductInExistingStore_Successful() {
        Product product1 = new Product("1", "1", "bgdfbf", "bdfgbfgds", 321, 3);
        Product product2 = new Product("1", "9", "bgdfbf", "bdfgbfgds", 321, 3);

        shoppingCart.addProduct(store, product1);
        shoppingCart.addProduct(store, product2);

        assertTrue(shoppingCart.getShoppingBags().size() == 1 & shoppingCart.getShoppingBags().get(0).getProducts().size() == 2);
    }


    @Test
    void addExistingProduct_Successful() {
        Product product = new Product("1", "1", "bgdfbf", "bdfgbfgds", 321, 3);

        shoppingCart.addProduct(store, product);
        shoppingCart.addProduct(store, product);

        assertTrue(shoppingCart.getShoppingBags().size() == 1 & shoppingCart.getShoppingBags().get(0).getProducts().get(product) == 2);
    }

    @Test
    void removeExistingProductLastInBag_Successful() {
        Product product = new Product("1", "1", "bgdfbf", "bdfgbfgds", 321, 3);

        shoppingCart.addProduct(store, product);
        shoppingCart.removeProduct(store, product);

        assertTrue(shoppingCart.getShoppingBags().size() == 0);
    }

    @Test
    void removeExistingProductNotLastInBag_Successful() {
        Product product1 = new Product("1", "1", "bgdfbf", "bdfgbfgds", 321, 3);
        Product product2 = new Product("1", "9", "bgdfbf", "bdfgbfgds", 321, 3);

        shoppingCart.addProduct(store, product1);
        shoppingCart.addProduct(store, product2);
        shoppingCart.removeProduct(store, product1);

        assertTrue(shoppingCart.getShoppingBags().size() == 1 & shoppingCart.getShoppingBags().get(0).getProducts().size() == 1);
    }

    @Test
    void removeNonExistingProduct_Failure() {
        Product product1 = new Product("1", "1", "bgdfbf", "bdfgbfgds", 321, 3);
        Product product2 = new Product("1", "1", "bgdfbf", "bdfgbfgds", 321, 3);
        shoppingCart.addProduct(store, product2);
        boolean result = shoppingCart.removeProduct(store, product2);

        assertFalse(result);
    }

    @Test
    void removeExistingProductInANonExistentStore() {
        Product product = new Product("1", "1", "bgdfbf", "bdfgbfgds", 321, 3);
        boolean result = shoppingCart.removeProduct(store, product);

        assertFalse(result);
    }




}
