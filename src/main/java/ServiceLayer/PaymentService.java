package ServiceLayer;

import DomainLayer.IPayment;
import DomainLayer.IProductRepository;
import DomainLayer.IStoreRepository;
import DomainLayer.User;
import DomainLayer.DomainServices.PaymentConnectivity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private IStoreRepository storeRepository;
    private IProductRepository productRepository;
    private PaymentConnectivity paymentConnectivity;

    public PaymentService(IStoreRepository storeRepository, IProductRepository productRepository, IPayment proxyPayment) {
        this.paymentConnectivity = new PaymentConnectivity(proxyPayment);
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    public boolean processPayment(User user, String storeId, String paymentService, String creditCardNumber, String expirationDate, String backNumber) {
        try {
            paymentConnectivity.processPayment(user, storeRepository.getStore(storeId), productRepository, creditCardNumber, expirationDate, backNumber, paymentService);
            EventLogger.logEvent(user.getID(), "Successfully payed for cart");
        return true;
        } catch (Exception e) {
            ErrorLogger.logError(user.getID(), "Failed to pay"     , e.getMessage());
            return false;
        }
    }
}