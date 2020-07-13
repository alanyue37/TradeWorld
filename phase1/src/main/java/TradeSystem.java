import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The entire trading system.
 */
public class TradeSystem {

    private final String tradeModelFile = "serializedobjects.ser";

    /**
     * Run the trading system.
     */
    public void run() {
        try {
            DataManager dataManager = new DataManager(tradeModelFile);
            TradeModel tradeModel = dataManager.readFromFile();
            LogInController controller = new LogInController(tradeModel);
            RunnableController mainController = controller.getNextController();
            mainController.run(); // This could be either UserController or AdminController
            dataManager.saveToFile(tradeModel);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
