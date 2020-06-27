import java.io.IOException;

/**
 * The entire trading system.
 */
public class TradeSystem {

    String serializedDataManagerInfo = "src/serializedobjects.ser";

    /**
     * Run the trading system.
     */
    public void run() {
        try {
            DataManager dm = new DataManager();
            TradeModel tm = dm.readFromFile(serializedDataManagerInfo);
            dm.saveToFile(serializedDataManagerInfo, tm);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
