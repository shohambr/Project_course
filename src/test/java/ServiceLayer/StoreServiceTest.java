package ServiceLayer;

import DomainLayer.IStoreRepository;
import DomainLayer.Store;
import DomainLayer.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class StoreServiceTest {

    private IStoreRepository storeRepository;
    private ProductService productService;
    private StoreService storeService;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        storeRepository = Mockito.mock(IStoreRepository.class);
        productService = Mockito.mock(ProductService.class);
        dummyUser = new User() {};
        storeService = new StoreService(storeRepository, productService);
    }

    @Test
    void addStore_ShouldCallAddStore() {

        Store store = new Store();

        storeService.addStore(store);

        verify(storeRepository, times(1)).addStore(store);
    }

    @Test
    void removeStore_ShouldCallRemoveStore_WhenStoreExists() {
        Store store = new Store();
        storeService.addStore(store);

        storeService.removeStore(store);

        verify(storeRepository, times(1)).removeStore(store);
    }
}
