package ServiceLayer;

import DomainLayer.domainServices.UserConnectivity;

public class RegisteredService {
    private final UserConnectivity userConnectivity;
    private final TokenService tokenService;

    public RegisteredService(UserConnectivity userConnectivity, TokenService tokenService) {
        this.userConnectivity = userConnectivity;
        this.tokenService = tokenService;
    }






        
    public String logoutRegistered(String token) throws Exception {
        String username = tokenService.extractUsername(token);
        try {
            userConnectivity.logout(username, token);
            EventLogger.logEvent(username, "LOGOUT");
            tokenService.invalidateToken(token);
            return tokenService.generateToken("Guest");
        }catch (IllegalArgumentException e) {
            EventLogger.logEvent(username, "LOGOUT_FAILED" );
            throw new RuntimeException("Invalid token");
        }
    }
}
