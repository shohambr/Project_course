package ServiceLayer;

import DomainLayer.IPayment;

public class PaymentService {

    private IPayment paymentSystem;

    public PaymentService(IPayment paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public boolean processPayment(String stringPayment, String creditCardNumber, String expirationDate, String backNumber) {
        try {
            Double payment = new Double(stringPayment);
            paymentSystem.processPayment(payment, creditCardNumber, expirationDate, backNumber);
        return true;
        } catch (Exception e) {
            System.out.println("Error encountered while processing payment:" + e.getMessage());
            return false;
        }
    }


}