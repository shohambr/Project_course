package DomainLayer.domainServices;

import DomainLayer.IPayment;
import DomainLayer.Store;
import DomainLayer.User;
import infrastructureLayer.ProxyPayment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentConnectivity {
    private IPayment proxyPayment;
    public PaymentConnectivity(IPayment proxyPayment) {
        this.proxyPayment = proxyPayment;
    }
    public void processPayment(User user, String creditCardNumber, String expirationDate, String backNumber, String paymentService) throws Exception {
        try {
            Map<Store, Double> stores = user.getShoppingCart().calculatePaymentStore();
            // should have a choice for payment company
            for (Store store: stores.keySet()) {
                proxyPayment.processPayment(stores.get(store), creditCardNumber, expirationDate, backNumber, store.getId(), paymentService);
            }
        } catch (Exception e) {
            throw new Exception("Exception for payment: " + e.getMessage());
        }
    }

}
