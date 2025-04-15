package ServiceLayer;

import DomainLayer.PaymentSystem;

public class PaymentService {

    private PaymentSystem paymentSystem;

    public PaymentService(PaymentSystem paymentSystem) {
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