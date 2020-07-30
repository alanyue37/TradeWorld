package loginadapters;

import adminadapters.AdminController;
import javafx.stage.Stage;
import tradegateway.TradeModel;
import trademisc.RunnableController;
import useradapters.UserController;
import usercomponent.UserTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The controller class that allows users to log in to the system.
 */
public class LogInController {

    private final TradeModel tradeModel;
    private final LogInPresenter presenter;
    private final LoginGUI gui;
    private String username;
    private String password;
    private RunnableController nextController = null;

    private volatile boolean loggedIn = false;

    /**
     * Creates a LogInController.
     * @param tradeModel the tradegateway.TradeModel containing all the information
     * @param stage The GUI JavaFX stage
     */
    public LogInController(TradeModel tradeModel, Stage stage) {
        this.tradeModel = tradeModel;
        gui = new LoginGUI(stage, 275, 300, this);
        presenter = new LogInPresenter();
        gui.loginInitialScreen();
    }

    /**
     * Method to get the next controller to run.
     * @return the next controller that will be run
     */
    public RunnableController getNextController() {
        if (nextController != null) {
            presenter.welcome(username);
            presenter.nextLine();
        }
        return nextController;
    }

    // Logging in
    boolean logIn(boolean isAdmin, String user, String pass) {
        username = user;
        password = pass;
        if ((!isAdmin && !tradeModel.getUserManager().login(username, password, UserTypes.TRADING)) || (isAdmin && !tradeModel.getUserManager().login(username, password, UserTypes.ADMIN))) {
            gui.invalidAccount();
            loggedIn = false;
            return false;
        }
        if (isAdmin && tradeModel.getUserManager().login(username, password, UserTypes.ADMIN)) {
            // Admin logged in
            nextController = new AdminController(tradeModel, username);
        }
        else if (!isAdmin && tradeModel.getUserManager().login(username, password, UserTypes.TRADING)) {
            // User logged in
            nextController = new UserController(tradeModel, username);
        }
        loggedIn = true;
        return true;
    }

    // Create new account
    boolean newTradingUser(String name, String user, String pass, String city) {
        username = user;
        password = pass;

        if (!tradeModel.getUserManager().createTradingUser(name, username, password, city)) {
            gui.usernameTaken(username);
            loggedIn = false;
            return false;
        }

        nextController = new UserController(tradeModel, username);
        loggedIn = true;
        return true;
    }
}