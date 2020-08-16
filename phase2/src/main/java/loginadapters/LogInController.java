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
     * @param tradeModel the TradeModel containing all the information
     */
    public LogInController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
    }

    /**
     * Checks if username and password is correct and returns account info Map of String pairs in JSON.
     * Checks key "authenticated" is true or false to see if username and password combination exists.
     * Checks key "userType" to see if account is "trading" or "admin".
     * @param user username to log in with
     * @param pass password to log in with
     * @return JSON string of login success and account info
     */
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

    /**
     * Create a new username with the given info.
     * @param name name of new user
     * @param user username of new user
     * @param pass password of new user
     * @param city city of new user
     * @return true if new account was created; false if username already exists
     */
    boolean newTradingUser(String name, String user, String pass, String city) {
        return tradeModel.getUserManager().createTradingUser(name, user, pass, city);
    }

}