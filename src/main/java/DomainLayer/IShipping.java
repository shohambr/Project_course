package DomainLayer;

public interface IShipping {
    void processShipping(String state, String city, String street, String homeNumber) throws Exception;
}