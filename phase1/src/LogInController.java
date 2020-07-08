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
            if (!selectMenu()) {
                return null;
            }
            return email;
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
        if ((!isAdmin && !userManager.login(email, password, userTypes.TRADING)) ||
                (isAdmin && !userManager.login(email, password, userTypes.ADMIN))) {
            presenter.invalidAccount();
            presenter.nextLine();
            logIn(isAdmin);
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
        if (!userManager.createTradingUser(name, email, password)) {
            presenter.emailTaken(email);
            presenter.nextLine();
            newTradingUser();
        }
    }
}