package DomainLayer;

public class ProxyPayment implements IPayment {
    private IPayment paymentSystem;
    public ProxyPayment(IPayment paymentSystem) {
        this.paymentSystem = paymentSystem;
    }
    public void processPayment(double payment, String creditCardNumber, String expirationDate, String backNumber) throws Exception {
        paymentSystem.processPayment(payment, creditCardNumber, expirationDate, backNumber);
    }

}
