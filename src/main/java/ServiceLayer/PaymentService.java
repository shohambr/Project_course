package ServiceLayer;

import DomainLayer.IPayment;
import DomainLayer.Store;
import DomainLayer.User;
import DomainLayer.domainServices.PaymentConnectivity;
import infrastructureLayer.ProxyPayment;

public class PaymentService {

    private PaymentConnectivity paymentConnectivity;

    public PaymentService(IPayment proxyPayment) {this.paymentConnectivity = new PaymentConnectivity(proxyPayment);}

    public boolean processPayment(User user, Store store, String paymentService, String payment, String creditCardNumber, String expirationDate, String backNumber) {
        try {
            paymentConnectivity.processPayment(payment, creditCardNumber, expirationDate, backNumber, store.getId(), paymentService);
            EventLogger.logEvent(user.getID(), "Successfully payed: " + payment);
        return true;
        } catch (Exception e) {
            System.out.println("Error encountered while processing payment:" + e.getMessage());
            ErrorLogger.logError(user.getID(), "Failed to pay"     , e.getMessage());
            return false;
        }
    }
}