import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The controller class that allows users to log in to the system.
 */
public class LogInController {

    private final TradeModel tradeModel;
    private final UserManager userManager;
    private final LogInPresenter presenter;
    private final BufferedReader br;
    private String username;
    private String password;
    private RunnableController nextController = null;


    /**
     * Creates a LogInController.
     * @param tm the TradeModel containing all the information
     */
    public LogInController(TradeModel tm) {
        tradeModel = tm;
        userManager = tradeModel.getUserManager();
        presenter = new LogInPresenter(tm);
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Method to get the next controller to run.
     * @return the next controller that will be run
     */
    public RunnableController getNextController() {
        try {
            selectMenu();
            return nextController;
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
        return null;
    }

//    The main menu
    private void selectMenu() throws IOException {
        presenter.startMenu();
        presenter.nextLine();
        String input = br.readLine();
        switch(input) {
            case "1":
                logIn(false);
                break;
            case "2":
                logIn(true);
                break;
            case "3":
                newTradingUser();
                break;
            case "exit":
                break;
            default:
                presenter.menuTryAgain();
                presenter.nextLine();
                selectMenu();
        }
    }

//    Logging in
    private void logIn(boolean isAdmin) throws IOException {
        presenter.logIn();
        presenter.nextLine();
        username = br.readLine();

        presenter.nextLine();
        password = br.readLine();

        if ((!isAdmin && !userManager.login(username, password, UserTypes.TRADING)) || (isAdmin && !userManager.login(username, password, UserTypes.ADMIN))) {
            presenter.invalidAccount();
            presenter.nextLine();
            selectMenu();
        }
        if (isAdmin && userManager.login(username, password, UserTypes.ADMIN)) {
            // Admin logged in
            nextController = new AdminController(tradeModel, username);
        }
        else if (!isAdmin && userManager.login(username, password, UserTypes.TRADING)) {
            nextController = new UserController(tradeModel, username);
        }
    }

//    Create new account
    private void newTradingUser() throws IOException {
        presenter.newAccount();
        presenter.nextLine();
        String name = br.readLine();

        presenter.nextLine();
        username = br.readLine();

        presenter.nextLine();
        password = br.readLine();

        if (!userManager.createTradingUser(name, username, password)) {
            presenter.usernameTaken(username);
            presenter.nextLine();
            selectMenu();
        }

        nextController = new UserController(tradeModel, username);
    }
}