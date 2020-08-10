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
    private String username;
    private String password;
    private RunnableController nextController = null;

    /**
     * Creates a LogInController.
     * @param tradeModel the tradegateway.TradeModel containing all the information
     */
    public LogInController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
    }

    /**
     * Method to get the next controller to run.
     * @return the next controller that will be run
     */
    public RunnableController getNextController() {
        return nextController;
    }

    // Logging in
    boolean logIn(boolean isAdmin, String user, String pass) {
        username = user;
        password = pass;
        if (tradeModel.getUserManager().login(username, password)) {
            if (tradeModel.getUserManager().isAdmin(username)) {
                nextController = (RunnableController) new AdminController(tradeModel, username); // Admin logged in
            }
            else {
                nextController = new UserController(tradeModel, username); // User logged in
            }
        }
        return true;
    }

    // Create new account
    boolean newTradingUser(String name, String user, String pass, String city) {
        username = user;
        password = pass;

        if (!tradeModel.getUserManager().createTradingUser(name, username, password, city)) {
            return false;
        }

        nextController = new UserController(tradeModel, username);
        return true;
    }

    void demo(){
        username = "DemoUser"; //TODO demo without username?
        nextController = new DemoController(tradeModel, username);
    }
}