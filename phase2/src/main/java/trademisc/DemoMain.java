package trademisc;

import javafx.application.Application;
import javafx.stage.Stage;
import tradegateway.DemoTradeSystem;

public class DemoMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        DemoTradeSystem ts = new DemoTradeSystem();
        ts.run(stage);
    }
}
