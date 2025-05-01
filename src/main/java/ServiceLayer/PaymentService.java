package ServiceLayer;

import DomainLayer.IPayment;
import DomainLayer.Store;
import DomainLayer.User;
import DomainLayer.domainServices.PaymentConnectivity;
import infrastructureLayer.ProxyPayment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private PaymentConnectivity paymentConnectivity;

    public PaymentService(IPayment proxyPayment) {this.paymentConnectivity = new PaymentConnectivity(proxyPayment);}

    public boolean processPayment(User user, String paymentService, String creditCardNumber, String expirationDate, String backNumber) {
        try {
            paymentConnectivity.processPayment(user, creditCardNumber, expirationDate, backNumber, paymentService);
            EventLogger.logEvent(user.getID(), "Successfully payed for cart");
        return true;
        } catch (Exception e) {
            System.out.println("Error encountered while processing payment:" + e.getMessage());
            ErrorLogger.logError(user.getID(), "Failed to pay"     , e.getMessage());
            return false;
        }
    }
}