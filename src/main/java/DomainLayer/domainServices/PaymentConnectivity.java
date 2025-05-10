package DomainLayer.DomainServices;

import DomainLayer.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import InfrastructureLayer.ProxyPayment;
import utils.ProductKeyModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentConnectivity {
    private final ObjectMapper mapper = new ObjectMapper();
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IPayment proxyPayment;

    public PaymentConnectivity(IPayment proxyPayment, IUserRepository userRepository, IProductRepository productRepository) {
        this.proxyPayment = proxyPayment;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mapper.registerModule(new ProductKeyModule());
        this.mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public void processPayment(String username, String creditCardNumber, String expirationDate, String backNumber, String paymentService) throws Exception {
        try {
            String jsonUser = userRepository.getUser(username);
            User user = mapper.readValue(jsonUser, User.class);
            List<ShoppingBag> shoppingBags = user.getShoppingCart().getShoppingBags();
            for (ShoppingBag shoppingBag : shoppingBags) {
                double payment = 0;
                    for (String product : shoppingBag.getProducts().keySet()) {
                        payment = payment + productRepository.getProduct(product).getPrice() * shoppingBag.getProducts().get(product);
                    }
                proxyPayment.processPayment(payment, creditCardNumber, expirationDate, backNumber, shoppingBag.getStoreId(), paymentService);
            }
        } catch (Exception e) {
            throw new Exception("Exception for payment: " + e.getMessage());
        }
    }

}
