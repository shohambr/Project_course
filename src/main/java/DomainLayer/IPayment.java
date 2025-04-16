package DomainLayer;

public interface IPayment{
    void processPayment(double payment, String creditCardNumber, String expirationDate, String backNumber) throws Exception;
}