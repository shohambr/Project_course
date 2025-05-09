package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.DiscountPolicy;
import DomainLayer.Product;

import java.util.*;



class DiscountPolicyTest {
    private Product tablet;
    private Product phone;
    private DiscountPolicy policy;

    @BeforeEach
    void setUp() {
        tablet = new Product(
                UUID.randomUUID().toString(),
                "store123",
                "Tablet",
                "High-end gaming tablet",
                100,
                10,
                4.5,
                "Electronics"
        );

        phone = new Product(
                UUID.randomUUID().toString(),
                "store123",
                "Phone",
                "Flagship smartphone",
                100,
                10,
                4.5,
                "Electronics"
        );

        policy = new DiscountPolicy();
    }

    @Test
    void testStackedProductDiscounts() {
        policy.addDiscount(1, -1, 2, new ArrayList<>(), 0.1f, "Tablet", -1, -1, "");
        policy.addDiscount(1, -1, 2, new ArrayList<>(), 0.2f, "Tablet", -1, -1, "");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(tablet, 1);
        cart.put(phone, 1);

        assertEquals(172.0f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testCategoryDiscount() {
        policy.addDiscount(2, -1, 2, new ArrayList<>(), 0.15f, "Electronics", -1, -1, "");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(tablet, 1);
        cart.put(phone, 2);

        assertEquals(255.0f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testQuantityConditionalDiscount() {
        policy.addDiscount(1, -1, 2, new ArrayList<>(), 0.2f, "Phone", 2, 3, "Phone");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(phone, 3);
        cart.put(tablet, 1);

        assertEquals(340.0f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testStoreWideDiscount() {
        policy.addDiscount(3, -1, 2, new ArrayList<>(), 0.1f, "", -1, -1, "");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(tablet, 2);
        cart.put(phone, 2);

        assertEquals(360.0f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testOrLogicDiscount() {
        List<Discount> nested = new ArrayList<>();
        nested.add(new Discount(1, -1, -1, new ArrayList<>(), 0f, "Tablet", 2, 1, "Tablet"));
        nested.add(new Discount(1, -1, -1, new ArrayList<>(), 0f, "Phone", 2, 1, "Phone"));

        policy.addDiscount(1, 3, 2, nested, 0.1f, "", -1, -1, "");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(tablet, 1);

        assertEquals(90.0f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testAndLogicDiscount() {
        List<Discount> nested = new ArrayList<>();
        nested.add(new Discount(-1, -1, -1, new ArrayList<>(), 0f, "", 2, 2, "Tablet"));
        nested.add(new Discount(-1, -1, -1, new ArrayList<>(), 0f, "", 1, 250, ""));

        policy.addDiscount(3, 2, -1, nested, 0.1f, "", -1, -1, "");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(tablet, 2);
        cart.put(phone, 1);

        assertEquals(270.0f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testStoreWideMinTotal() {
        policy.addDiscount(3, -1, -1, new ArrayList<>(), 0.1f, "", 1, 300, "");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(tablet, 3);

        assertEquals(270.0f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testCategoryProductStacking() {
        policy.addDiscount(2, -1, 2, new ArrayList<>(), 0.1f, "Electronics", -1, -1, "");
        policy.addDiscount(1, -1, 2, new ArrayList<>(), 0.05f, "Tablet", -1, -1, "");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(tablet, 1);

        assertEquals(85.5f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testConditionalQuantityNotMet() {
        policy.addDiscount(1, -1, 2, new ArrayList<>(), 0.2f, "Phone", 2, 3, "Phone");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(phone, 2);
        cart.put(tablet, 1);

        assertEquals(300.0f, policy.applyDiscounts(cart), 0.001f);
    }

    @Test
    void testMaximumNestedDiscount() {
        List<Discount> nested = new ArrayList<>();
        nested.add(new Discount(1, -1, 2, new ArrayList<>(), 0.1f, "Tablet", -1, -1, ""));
        nested.add(new Discount(1, -1, 2, new ArrayList<>(), 0.2f, "Tablet", -1, -1, ""));

        policy.addDiscount(1, -1, 1, nested, 0f, "Tablet", -1, -1, "");

        Map<Product, Integer> cart = new HashMap<>();
        cart.put(tablet, 1);

        assertEquals(80.0f, policy.applyDiscounts(cart), 0.001f);
    }
}
