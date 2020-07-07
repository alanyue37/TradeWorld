import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogInController {

    private final UserManager userManager;
    private final LogInPresenter presenter;
    private final BufferedReader br;
    private String email;
    private String password;

    public LogInController(UserManager um) {
        userManager = um;
        presenter = new LogInPresenter(userManager);
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public String run() {
        try {
            selectMenu();
            return email;
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
        return null;
    }

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

    private void logIn(boolean isAdmin) throws IOException {
        presenter.logIn();
        presenter.nextLine();
        email = br.readLine();

        presenter.nextLine();
        password = br.readLine();

        if ((!isAdmin && !userManager.trader_login(email, password)) || (isAdmin && !userManager.admin_login(email, password))) {
            presenter.invalidAccount();
            presenter.nextLine();
            logIn(isAdmin);
        }
    }

    private void newTradingUser() throws IOException {
        presenter.newAccount();
        presenter.nextLine();
        String name = br.readLine();

        presenter.nextLine();
        email = br.readLine();

        presenter.nextLine();
        password = br.readLine();

        if (!userManager.createTradingUser(name, email, password)) {
            presenter.emailTaken(email);
            presenter.nextLine();
            newTradingUser();
        }
    }
}