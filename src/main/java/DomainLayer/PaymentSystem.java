package DomainLayer;

public interface PaymentSystem {
    void processPayment(double payment, String creditCardNumber, String expirationDate, String backNumber) throws Exception;
}
