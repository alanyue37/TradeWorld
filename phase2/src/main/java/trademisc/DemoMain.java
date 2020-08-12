package trademisc;

import javafx.application.Application;
import javafx.stage.Stage;
import tradegateway.DemoTradeSystem;

public class DemoMain extends Application {

    private DemoTradeSystem ts;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ts = new DemoTradeSystem();
        ts.run(stage);
    }

    @Override
    public void stop(){
        ts.persist();
    }
}
