import java.io.IOException;
import java.net.URISyntaxException;

/**
 * DEMO trading system with pre-inserted users, items, etc. solely for internal testing
 */
public class DemoTradeSystem {

    private final String tradeModelFile = "serializedobjects.ser";

    /**
     * Run the trading system.
     */
    public void run() {
        try {
            DataManager dataManager = new DataManager(tradeModelFile);
            TradeModel tradeModel = dataManager.readFromFile();

            // DEMO  --- START
            tradeModel.getUserManager().createAdminUser("A_1", "a1", "a1");
            tradeModel.getUserManager().createAdminUser("A_2", "a2", "a2");
            tradeModel.getUserManager().createAdminUser("A_3", "a3", "a3");
            tradeModel.getUserManager().createTradingUser("U_1", "u1", "u1");
            tradeModel.getUserManager().createTradingUser("U_2", "u2", "u2");
            tradeModel.getUserManager().createTradingUser("U_3", "u3", "u3");
            tradeModel.getUserManager().createTradingUser("U_4", "u4", "u4");

            // u1 items
            String u1_1 = tradeModel.getItemManager().addItem("red apple", "u1", "One red apple");
            String u1_2 = tradeModel.getItemManager().addItem("green apple", "u1", "One green apple");
            String u1_3 = tradeModel.getItemManager().addItem("yellow apple", "u1", "One yellow apple");

            // u2 items
            String u2_1 = tradeModel.getItemManager().addItem("red car", "u2", "One red car");
            String u2_2 = tradeModel.getItemManager().addItem("green car", "u2", "One green car");
            String u2_3 = tradeModel.getItemManager().addItem("yellow car", "u2", "One yellow car");

            // u3 items
            String u3_1 = tradeModel.getItemManager().addItem("red book", "u3", "One red book");
            String u3_2 = tradeModel.getItemManager().addItem("green book", "u3", "One green book");
            String u3_3 = tradeModel.getItemManager().addItem("yellow book", "u3", "One yellow book");

            // u4 items
            String u4_1 = tradeModel.getItemManager().addItem("red ball", "u4", "One red ball");
            String u4_2 = tradeModel.getItemManager().addItem("green ball", "u4", "One green ball");
            String u4_3 = tradeModel.getItemManager().addItem("yellow ball", "u4", "One yellow ball");

            // Confirm all of u1 items (apples)
            tradeModel.getItemManager().confirmItem(u1_1);
            tradeModel.getItemManager().confirmItem(u1_2);
            tradeModel.getItemManager().confirmItem(u1_3);

            // Confirm all of u2 items (cars)
            tradeModel.getItemManager().confirmItem(u2_1);
            tradeModel.getItemManager().confirmItem(u2_2);
            tradeModel.getItemManager().confirmItem(u2_3);

            // Confirm ONLY first two of u3 items (red and green books)
            tradeModel.getItemManager().confirmItem(u3_1);
            tradeModel.getItemManager().confirmItem(u3_2);

            // Confirm ONLY first two of u4 items (red and green balls)
            tradeModel.getItemManager().confirmItem(u4_1);
            tradeModel.getItemManager().confirmItem(u4_2);

            // DEMO  --- START


            LogInController controller = new LogInController(tradeModel);
            RunnableController mainController = controller.getNextController();
            if (mainController != null) {
                mainController.run(); // This could be either UserController or AdminController
            }
            dataManager.saveToFile(tradeModel);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
