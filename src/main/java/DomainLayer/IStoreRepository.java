package DomainLayer;

public interface IStoreRepository {
    void addStore(String storeId , String storeJson);
    void removeStore(String store);
    String getStore(String storeId);
    void updateStore(String storeId, String storeJson);
}
