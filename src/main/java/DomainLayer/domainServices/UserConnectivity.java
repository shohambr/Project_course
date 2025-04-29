package DomainLayer.domainServices;

import DomainLayer.User;
import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import ServiceLayer.EventLogger;
import io.micrometer.observation.Observation.Event;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;


public class UserConnectivity {
    private IToken Tokener;
    private IUserRepository userRepository;

    public UserConnectivity(IToken Tokener, IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.Tokener = Tokener;
    }


    
    public void login(String username, String password ,String hashedpass) {
        if (username == null || password == null ) {
            EventLogger.logEvent(username, "LOGIN_FAILED - NULL");
            throw new IllegalArgumentException("Username and password cannot be null");
        }else if (username.isEmpty() || password.isEmpty()) {
            EventLogger.logEvent(username, "LOGIN_FAILED - EMPTY");
            throw new IllegalArgumentException("Username and password cannot be empty");
        }
        if(hashedpass == null){
            EventLogger.logEvent(username, "LOGIN_FAILED - USER_NOT_EXIST");
            throw new IllegalArgumentException("User does not exist");
        }
        if (!BCrypt.checkpw(password, hashedpass)) {
            EventLogger.logEvent(username, "LOGIN_FAILED - WRONG_PASSWORD");
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    public void signUp(String username, String password) {
        if (username == null || password == null) {
            EventLogger.logEvent(username, "SIGNUP_FAILED - NULL");
            throw new IllegalArgumentException("Username and password cannot be null");
        } else if (username.isEmpty() || password.isEmpty()) {
            EventLogger.logEvent(username, "SIGNUP_FAILED - EMPTY");
            throw new IllegalArgumentException("Username and password cannot be empty");
        }
        if (userRepository.isUserExist(username)) {
            EventLogger.logEvent(username, "SIGNUP_FAILED - USER_EXIST");
            throw new IllegalArgumentException("User already exists");
        }
    }

    public void logout(String username ,String token) {
        if(username.equals("Guest")) {
            EventLogger.logEvent(username, "LOGOUT_FAILED - GUEST");
            throw new IllegalArgumentException("Guest cannot logout");
        }
        if (username == null) {
            EventLogger.logEvent(username, "LOGOUT_FAILED - USER_NULL");
            throw new IllegalArgumentException("Username cannot be null");
        } else if (username.isEmpty()) {
            EventLogger.logEvent(username, "LOGOUT_FAILED - USER_EMPTY");
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (token == null) {
            EventLogger.logEvent(username, "LOGOUT_FAILED - TOKEN_NULL");
            throw new IllegalArgumentException("Token cannot be null");
        } else if (token.isEmpty()) {
            EventLogger.logEvent(username, "LOGOUT_FAILED - TOKEN_EMPTY");
            throw new IllegalArgumentException("Token cannot be empty");
        }
        EventLogger.logEvent(username, "LOGOUT_SUCCESS");
    }
}
