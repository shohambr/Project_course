package ServiceLayer;

import DomainLayer.IPayment;
import DomainLayer.User;

public class PaymentService {

    private IPayment paymentSystem;

    public PaymentService(IPayment paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public boolean processPayment(User user, String stringPayment, String creditCardNumber, String expirationDate, String backNumber) {
        try {
            Double payment = new Double(stringPayment);
            paymentSystem.processPayment(payment, creditCardNumber, expirationDate, backNumber);
            EventLogger.logEvent(user.getID(), "Successfully payed: " + stringPayment);
        return true;
        } catch (Exception e) {
            System.out.println("Error encountered while processing payment:" + e.getMessage());
            ErrorLogger.logError(user.getID(), "Failed to pay"     , e.getMessage());
            return false;
        }
    }


}