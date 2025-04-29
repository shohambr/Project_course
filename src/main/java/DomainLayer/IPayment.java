package DomainLayer;

public interface IPayment{
    void processPayment(String payment, String creditCardNumber, String expirationDate, String backNumber, String storeId, String paymentService) throws Exception;
}