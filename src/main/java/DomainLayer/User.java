package DomainLayer;
import java.util.UUID;
import DomainLayer.Roles.Guest;
import DomainLayer.Roles.RegisteredUser;
import ServiceLayer.UserService;

public abstract class User extends Guest{

    public User() {
    }

}