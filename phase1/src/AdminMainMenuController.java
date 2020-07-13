/*
import java.util.*;

public class AdminMainMenuController extends MenuTextController {

    public AdminMainMenuController(TradeModel tradeModel, String username) {
        super(tradeModel, username);
    }

    @Override
    public void run() {
        //List<String> options = Arrays.asList(new String[] {"Create new admin", "Review items"});
        RunnableController r1 = new AdminMainMenuController(new TradeModel(), "jey");
        RunnableController r2 = new AdminMainMenuController(new TradeModel(), "abc");
        List<RunnableController> controllers = Arrays.asList(new RunnableController[] {r1, r2});
        addNextControllers(controllers);
        displayMenu();
    }
}
*/
