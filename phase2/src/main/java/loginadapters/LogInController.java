package loginadapters;

import adminadapters.AdminController;
import tradegateway.TradeModel;
import trademisc.RunnableController;
import useradapters.DemoController;
import useradapters.UserController;

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

    // Logging in
    boolean logIn(boolean isAdmin, String user, String pass) {
        return tradeModel.getUserManager().login(user, pass);
    }

    // Create new account
    boolean newTradingUser(String name, String user, String pass, String city) {
        return tradeModel.getUserManager().createTradingUser(name, user, pass, city);
    }

//    void demo(){
//        String username = "DemoUser";
//        nextController = new DemoController(tradeModel, username);
//    }
}