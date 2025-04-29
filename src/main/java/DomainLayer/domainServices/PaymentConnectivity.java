package DomainLayer.domainServices;

import DomainLayer.IPayment;
import infrastructureLayer.ProxyPayment;

public class PaymentConnectivity {
    private IPayment proxyPayment;
    public PaymentConnectivity(IPayment proxyPayment) {
        this.proxyPayment = proxyPayment;
    }
    public void processPayment(String payment, String creditCardNumber, String expirationDate, String backNumber, String storeId, String paymentService) throws Exception {
        try {
            Double doublePayment = new Double(payment);
            if (doublePayment < 0) {
                throw new Exception("Negative payment is not allowed");
            }
            // should have a choice for payment company
            proxyPayment.processPayment(payment, creditCardNumber, expirationDate, backNumber, storeId, paymentService);
        } catch (Exception e) {
            throw new Exception("Exception for payment: " + e.getMessage());
        }
    }

}
