package loginadapters;

import adminadapters.AdminController;
import trademisc.RunnableController;
import tradegateway.TradeModel;
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
    private final BufferedReader br;
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
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Method to get the next controller to run.
     * @return the next controller that will be run
     */
    public RunnableController getNextController() {
        try {
            selectMenu();
            if (nextController != null) {
                presenter.welcome(username);
                presenter.nextLine();
            }
            return nextController;
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
        return null;
    }

    // The main menu
    private void selectMenu() throws IOException {
        boolean validInput = false;
        do {
            presenter.startMenu();
            presenter.nextLine();
            String input = br.readLine();
            switch (input) {
                case "1":
                    validInput = logIn(false);
                    break;
                case "2":
                    validInput = logIn(true);
                    break;
                case "3":
                    validInput = newTradingUser();
                    break;
                case "exit":
                    validInput = true;
                    break;
                default:
                    presenter.menuTryAgain();
                    presenter.nextLine();
            }
        } while (!validInput);

    }

    // Logging in
    private boolean logIn(boolean isAdmin) throws IOException {
        presenter.logIn();
        presenter.nextLine();
        username = br.readLine();

        presenter.nextLine();
        password = br.readLine();

        if ((!isAdmin && !tradeModel.getUserManager().login(username, password, UserTypes.TRADING)) || (isAdmin && !tradeModel.getUserManager().login(username, password, UserTypes.ADMIN))) {
            presenter.invalidAccount();
            presenter.nextLine();
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
        return true;
    }

    // Create new account
    private boolean newTradingUser() throws IOException {
        presenter.newAccount();
        presenter.nextLine();
        String name = br.readLine();

        presenter.nextLine();
        username = br.readLine();

        presenter.nextLine();
        password = br.readLine();

        if (!tradeModel.getUserManager().createTradingUser(name, username, password)) {
            presenter.usernameTaken(username);
            presenter.nextLine();
            return false;
        }

        nextController = new UserController(tradeModel, username);
        return true;
    }
}