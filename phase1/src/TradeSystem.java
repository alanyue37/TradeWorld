import java.io.IOException;
/**
 * The entire trading system.
 */
public class TradeSystem {
    String serializedDataManagerInfo = "serializedobjects.ser";
    /**
     * Run the trading system.
     */
    public void run() {
        try {
            DataManager dataManager = new DataManager();
            TradeModel tradeModel = dataManager.readFromFile(serializedDataManagerInfo);
            // DEMO CODE
            tradeModel.getUserManager().createAdminUser("a1", "a1", "a1");
            tradeModel.getUserManager().createAdminUser("a2", "a2", "a2");
            tradeModel.getUserManager().createTradingUser("u1", "u1", "u1");
            tradeModel.getUserManager().createTradingUser("u2", "u2", "u2");
            System.out.println(tradeModel.getUserManager());
            LogInController controller = new LogInController(tradeModel);
            RunnableController mainController = controller.getNextController();
            mainController.run(); // This could be either UserController or AdminController
            dataManager.saveToFile(serializedDataManagerInfo, tradeModel);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (NullPointerException exception) {
            // Exit menu option selected
            // TODO: Create custom exception (e.g. NoNextController)
            System.out.println("Bye!");
        }
    }
}