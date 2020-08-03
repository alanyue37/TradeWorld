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
import java.util.Observable;

/**
 * The controller class that allows users to log in to the system.
 */
public class LogInController extends Observable {

    private final TradeModel tradeModel;
    private final LogInPresenter presenter;
    private String username;
    private String password;
    private RunnableController nextController = null;

    /**
     * Creates a LogInController.
     * @param tradeModel the tradegateway.TradeModel containing all the information
     */
    public LogInController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
        presenter = new LogInPresenter();
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
        if (tradeModel.getUserManager().login(username, password)) {
            if (tradeModel.getUserManager().isAdmin(username)) {
                nextController = new AdminController(tradeModel, username); // Admin logged in
            }
            else {
                nextController = new UserController(tradeModel, username); // User logged in
            }
        }
        setChanged();
        notifyObservers();
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
        setChanged();
        notifyObservers();
        return true;
    }
}