package DomainLayer;

public interface IUserRepository {

                                     //Easy signatures that should always be in any UserRepository implementation ever (we won't need to implement more tho)
    public boolean addUser(String username, String password);
    public String getUserPass(String username);
    public boolean isUserExist(String username);
}