package DomainLayer.DomainServices;

import DomainLayer.*;
import infrastructureLayer.ProxyPayment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentConnectivity {
    private IPayment proxyPayment;
    public PaymentConnectivity(IPayment proxyPayment) {
        this.proxyPayment = proxyPayment;
    }
    public void processPayment(User user, Store store, IProductRepository productRepository, String creditCardNumber, String expirationDate, String backNumber, String paymentService) throws Exception {
        try {
            List<ShoppingBag> shoppingBags = user.getShoppingCart().getShoppingBags();
            // should have a choice for payment company
            for (ShoppingBag shoppingBag : shoppingBags) {
                double payment = 0;
                if (shoppingBag.getStoreId().equals(store.getId() + "" + "")) {
                    for (String product : shoppingBag.getProducts().keySet()) {
                        payment = payment + productRepository.getProduct(product).getPrice() * shoppingBag.getProducts().get(product);
                    }
                }
                proxyPayment.processPayment(payment, creditCardNumber, expirationDate, backNumber, paymentService);
            }
        } catch (Exception e) {
            throw new Exception("Exception for payment: " + e.getMessage());
        }
    }

}
