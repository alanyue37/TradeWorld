package loginadapters;

import com.google.gson.Gson;
import tradegateway.TradeModel;

import java.util.HashMap;
import java.util.Map;

/**
 * The controller class that allows users to log in to the system.
 */
public class LogInController {

    private final TradeModel tradeModel;

    /**
     * Creates a LogInController.
     * @param tradeModel the tradegateway.TradeModel containing all the information
     */
    public LogInController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
    }

    // Logging in - returns JSON
    public String logIn(String user, String pass) {
        Gson gson = new Gson();
        Map<String, String> userInfo = new HashMap<>();
        if (tradeModel.getUserManager().login(user, pass)) {
            tradeModel.setCurrentUser(user);
            userInfo.put("authenticated", "true");
            if (tradeModel.getUserManager().isAdmin(user)) {
                userInfo.put("userType", "admin");
            }
            else {
                userInfo.put("userType", "trading");
            }
        }
        else {
            userInfo.put("authenticated", "false");
        }
        return gson.toJson(userInfo);
    }

    // Create new account
    boolean newTradingUser(String name, String user, String pass, String city) {
        return tradeModel.getUserManager().createTradingUser(name, user, pass, city);
    }

}