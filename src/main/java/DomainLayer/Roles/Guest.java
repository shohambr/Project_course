package DomainLayer.Roles;

import DomainLayer.User;
import ServiceLayer.UserService;

public class Guest extends User {

    public Guest() {
        super();
    }
    

    public void logout() {
        throw new UnsupportedOperationException("Guest cannot logout");
    }



}
