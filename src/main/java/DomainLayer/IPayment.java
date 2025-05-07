package DomainLayer;

public interface IPayment{
    void processPayment(Double payment, String creditCardNumber, String expirationDate, String backNumber, String paymentService) throws Exception;
}