package DomainLayer;

public interface IShipping {
    void processShipping(User user, Store store, String state, String city, String street, String homeNumber) throws Exception;
}