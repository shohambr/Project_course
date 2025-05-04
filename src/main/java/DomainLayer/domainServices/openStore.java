package DomainLayer.domainServices;
import DomainLayer.IStoreRepository;
import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import DomainLayer.Store;
import com.fasterxml.jackson.databind.ObjectMapper;

public class openStore {
    private IToken Tokener;
    private IStoreRepository storeRepository;
    private IUserRepository userRepository;
    private ObjectMapper mapper = new ObjectMapper();

    public openStore(IToken Tokener, IStoreRepository storeRepository, IUserRepository userRepository) {
        this.Tokener = Tokener;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    public String openStore(String token) throws Exception {
        if (token == null) {
            throw new IllegalArgumentException("Invalid input");
        }
        Tokener.validateToken(token);
        String username = Tokener.extractUsername(token);
        if (userRepository.getUser(username) == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        Store store = new Store(username);
        storeRepository.addStore(store.getId(), mapper.writeValueAsString(store));
        return store.getId();
    }
}
