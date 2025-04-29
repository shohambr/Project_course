package DomainLayer.domainServices;

import DomainLayer.IPayment;
import DomainLayer.Store;
import infrastructureLayer.ProxyPayment;

import java.util.List;

public class PaymentConnectivity {
    private IPayment proxyPayment;
    public PaymentConnectivity(IPayment proxyPayment) {
        this.proxyPayment = proxyPayment;
    }
    public void processPayment(String payment, String creditCardNumber, String expirationDate, String backNumber, List<Store> stores, String paymentService) throws Exception {
        try {
            Double doublePayment = new Double(payment);
            if (doublePayment < 0) {
                throw new Exception("Negative payment is not allowed");
            }
            // should have a choice for payment company
            for (Store store: stores) {
                proxyPayment.processPayment(payment, creditCardNumber, expirationDate, backNumber, store.getId(), paymentService);
            }
        } catch (Exception e) {
            throw new Exception("Exception for payment: " + e.getMessage());
        }
    }

}
