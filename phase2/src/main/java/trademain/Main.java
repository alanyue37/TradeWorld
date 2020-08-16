package trademain;

import javafx.application.Application;
import javafx.stage.Stage;

import tradegateway.TradeSystem;

public class Main extends Application {

    private TradeSystem ts;

    @Override
    public void start(Stage stage) {
        ts = new TradeSystem();
        ts.run(stage);
    }

    @Override
    public void stop(){
        ts.persist();
    }
}
