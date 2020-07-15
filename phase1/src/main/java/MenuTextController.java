import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MenuTextController extends TextController {
    private Map<Integer, RunnableController> nextControllers;
    private Map<Integer, String> menuOptions;
    private MenuTextPresenter presenter;

    /**
     * Initiates a new meeting
     * @param tradeModel    The tradeModel
     * @param username      The username of the User
     * @param presenter     The present
     */
    public MenuTextController(TradeModel tradeModel, String username, MenuTextPresenter presenter) {
        super(tradeModel, username);
        nextControllers = new HashMap<>();
        menuOptions = new HashMap<>();
        this.presenter = presenter;

    }

    public void addMenuOptionsAndNextControllers(List<String> options, List<RunnableController> controllers) {
        // length must be equal of both lists
        for (int i=0; i < options.size(); i++) {
            menuOptions.put(i+1, options.get(i));
            nextControllers.put(i+1, controllers.get(i));
        }
    }

    public void addNextControllers(List<RunnableController> controllers) {
        // length must be equal of both lists
        int i = 1;
        for (RunnableController controller : controllers) {
            nextControllers.put(i, controller);
        }
    }

    public RunnableController getController(int choice) {
        return nextControllers.get(choice - 1);
    }

}
