package DomainLayer.Roles;

import DomainLayer.User;

public class Guest extends User {

    @Override
    public String logout(String u , String p) {
        return null;
    }

    public void test(){
        you = market.enterGuest();
        you = you.login(username , password);
    }

}
