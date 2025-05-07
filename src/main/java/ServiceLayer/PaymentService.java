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

    // private IStoreRepository storeRepository;
    // private IProductRepository productRepository;
    // private PaymentConnectivity paymentConnectivity;

    // public PaymentService(IStoreRepository storeRepository, IProductRepository productRepository, IPayment proxyPayment) {
    //     this.paymentConnectivity = new PaymentConnectivity(proxyPayment);
    //     this.storeRepository = storeRepository;
    //     this.productRepository = productRepository;
    // }

    // public boolean processPayment(String userId, String storeId, String paymentService, String creditCardNumber, String expirationDate, String backNumber) {
    //     try {
    //         paymentConnectivity.processPayment(userId, productRepository, creditCardNumber, expirationDate, backNumber, paymentService);
    //         EventLogger.logEvent(userId, "Successfully payed for cart");
    //     return true;
    //     } catch (Exception e) {
    //         ErrorLogger.logError(userId, "Failed to pay"     , e.getMessage());
    //         return false;
    //     }
    // }
}