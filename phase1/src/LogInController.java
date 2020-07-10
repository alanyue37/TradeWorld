import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogInController {

    private final TradeModel tradeModel;
    private final LogInPresenter presenter;
    private final BufferedReader br;
    private String email;
    private String password;
    private RunnableController nextController = null;

    public LogInController(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
        presenter = new LogInPresenter();
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public RunnableController getNextController() {
        try {
            while (nextController == null) {
                selectMenu();
            }
            return nextController;
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
        return null;
    }

    private boolean selectMenu() throws IOException {
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
                return false;
            default:
                presenter.menuTryAgain();
                presenter.nextLine();
                selectMenu();
        }
        return true;
    }

    private void logIn(boolean isAdmin) throws IOException {
        UserManager userManager = tradeModel.getUserManager();
        presenter.logIn();
        presenter.nextLine();
        email = br.readLine();
        if (email.equals("exit")) {
            //do something
            return;
        } else if (email.equals("back")) {
            //do something
            return;
        }
        presenter.nextLine();
        password = br.readLine();
        if (password.equals("exit")) {
            //do something
            return;
        } else if (password.equals("back")) {
            //do something
            return;
        }

        if (isAdmin && userManager.login(email, password, userTypes.ADMIN)) {
            // Admin logged in
            nextController = new AdminController(tradeModel);
        }
        else if (!isAdmin && userManager.login(email, password, userTypes.TRADING)) {
            nextController = new UserController(tradeModel);
        }
    }

    private void newTradingUser() throws IOException {
        presenter.newAccount();
        presenter.nextLine();
        String name = br.readLine();
        if (name.equals("exit")) {
            //do something
            return;
        } else if (name.equals("back")) {
            //do something
            return;
        }
        presenter.nextLine();
        email = br.readLine();
        if (email.equals("exit")) {
            //do something
            return;
        } else if (email.equals("back")) {
            //do something
            return;
        }
        presenter.nextLine();
        password = br.readLine();
        if (password.equals("exit")) {
            //do something
            return;
        } else if (password.equals("back")) {
            //do something
            return;
        }
        if (tradeModel.getUserManager().createTradingUser(name, email, password)) {
            presenter.emailTaken(email);
            presenter.nextLine();
            newTradingUser();
        }
    }
}