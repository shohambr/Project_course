package DomainLayer.Roles;

import DomainLayer.User;

public class Guest extends User {

    @Override
    public void logout() {
        throw new UnsupportedOperationException("Guest cannot logout");
    }



}
