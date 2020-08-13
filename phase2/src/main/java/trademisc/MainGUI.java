package trademisc;

import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import tradeadapters.TradeGUI;
import tradegateway.TradeModel;

public abstract class MainGUI implements RunnableGUI {

    private final Stage stage;
    private final TradeModel tradeModel;
    private final int width;
    private final int height;

    public MainGUI(int width, int height, TradeModel tradeModel) {
        this.stage = new Stage();
        this.tradeModel = tradeModel;
        this.width = width;
        this.height = height;
    }

    protected Stage getStage() {
        return stage;
    }

    protected TradeModel getTradeModel() {
        return tradeModel;
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }

}
