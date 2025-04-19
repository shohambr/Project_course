package DomainLayer.Roles;

import DomainLayer.User;
import ServiceLayer.UserService;

public class Guest extends User {

    public Guest() {
        super();
    }
    

    @Override
    public void logout() {
        throw new UnsupportedOperationException("Guest cannot logout");
    }



}
