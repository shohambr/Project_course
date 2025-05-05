package DomainLayer;

public interface IStoreRepository {
    void addStore(Store store);
    void removeStore(Store store);
    Store getStore(String storeId);
    void updateStore(String storeId, String storeJson);
}
