package trademain;//hello!

import javafx.application.Application;
import javafx.stage.Stage;
import tradegateway.TradeSystem;

public class Main extends Application{

    private TradeSystem ts;

    public static void main(String[] args) {
        launch(args);
    }

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