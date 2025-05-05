package UILayer;

import DomainLayer.*;
import ServiceLayer.*;
import infrastructureLayer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfiguration {

    @Bean
    public JobRepository JobRepository() {
        return new JobRepository();
    };

    @Bean
    public OrderRepository OrderRepository() {
        return new OrderRepository();
    };

    @Bean
    public ProductRepository ProductRepository() {
        return new ProductRepository();
    };

    @Bean
    public ProxyPayment ProxyPayment() {
        return new ProxyPayment();
    };

    @Bean
    public ProxyShipping ProxyShipping() {
        return new ProxyShipping();
    };

    @Bean
    public StoreRepository StoreRepository() {
        return new StoreRepository();
    };

    @Bean
    public UserRepository UserRepository() {
        return new UserRepository();
    };

    @Bean
    public JobService JobService() {
        return new JobService(JobRepository(), StoreService());
    };

    @Bean
    public NotificationService NotificationService() {
        return new NotificationService();
    };

    @Bean
    public OrderService OrderService() {
        return new OrderService(OrderRepository());
    };

    @Bean
    public PaymentService PaymentService() {
        return new PaymentService(ProxyPayment());
    };

    @Bean
    public ProductService ProductService() {
        return new ProductService(ProductRepository());
    };

    @Bean
    public ShippingService ShippingService() {
        return new ShippingService(ProxyShipping());
    };

    @Bean
    public StoreService StoreService() {
        return new StoreService(StoreRepository(), ProductService());
    };

    @Bean
    public TokenService TokenService() {
        return new TokenService();
    };

    @Bean
    public UserService UserService() {
        return new UserService(UserRepository(), TokenService(),JobService(), ProductService(), StoreRepository(), ProductRepository(), ProxyPayment(), OrderRepository());
    };

    @Bean
    public RegisteredService RegisteredService() {
        return new RegisteredService(UserRepository(), TokenService() , StoreRepository(), ProductRepository(), OrderRepository());
    };



}
