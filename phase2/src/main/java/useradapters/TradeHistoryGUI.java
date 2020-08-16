package useradapters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tradegateway.TradeModel;
import trademain.RunnableGUI;

import java.util.List;
import java.util.Map;

public class TradeHistoryGUI implements RunnableGUI {
    private GridPane root;
    private final Stage stage;
    private final int width;
    private final int height;
    private final TradeModel tradeModel;
    private final String username;
    private Gson gson;

    public TradeHistoryGUI(Stage stage, int width, int height, TradeModel model, String username){
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.tradeModel = model;
        this.username = username;
        gson = new Gson();
    }

    @Override
    public Parent getRoot() {
        initializeScreen();
        return root;
    }

    @Override
    public void showScreen() {
        initializeScreen();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeScreen() {
        root = new GridPane();

        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));

        Label titleLabel = new Label(username + "'s Trading History");
        titleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        root.add(titleLabel, 0, 0, 2, 1);
        root.add(new Label("Items"), 0, 1);
        root.add(new Label("Trading Partners"), 1, 1);
        root.add(getItemHistory(10), 0, 2);
        root.add(getPartnerHistory(10), 1, 2);
    }

    private ListView<String> getItemHistory(int count) {
        ListView<String> result = new ListView<>();
        result.prefWidthProperty().bind(stage.widthProperty());
        result.setPlaceholder(new Label("No past trades to display."));

        List<String> trades = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        try {
            if (trades.size() > 0){
                for (int i = trades.size() - 1; i > trades.size() - count; i--) {
                    JSONObject tradeInfo = tradeModel.getTradeManager().getTradeInfo(trades.get(i));
                    StringBuilder builder = new StringBuilder();
                    JSONArray items = (JSONArray) tradeInfo.get("Items involved");
                    Map<String, String> itemInfo = gson.fromJson(tradeModel.getItemManager().getItemInfoJSON(items.getString(0)), new TypeToken<Map<String, String>>() {}.getType());
                    builder.append(itemInfo.get("name"));
                    result.getItems().add(builder.toString());
                }
            }
        } catch (JSONException e){
            result.getItems().add("An error occurred.");
        } catch (IndexOutOfBoundsException ignored) {} // Ignored because if user has fewer than 10 trades in history, we show all of them
        return result;
    }

    private ListView<String> getPartnerHistory(int count) {
        ListView<String> result = new ListView<>();
        result.prefWidthProperty().bind(stage.widthProperty());
        result.setPlaceholder(new Label("No past trading partners to display."));

        List<String> trades = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        try {
            if (trades.size() > 0) {
                for (int i = trades.size() - 1; i > trades.size() - count; i--) {
                    JSONObject tradeInfo = tradeModel.getTradeManager().getTradeInfo(trades.get(i));
                    JSONArray items = (JSONArray) tradeInfo.get("Users involved");
                    for (int j = 0; j < items.length(); j++){
                        String name = items.getString(j);
                        if (!name.equals(username)){
                            result.getItems().add(name);
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e){
            result.getItems().add("An error occurred.");
        } catch (IndexOutOfBoundsException ignored) {} // Ignored because if user has fewer than 10 trades in history, we show all of them
        return result;
    }
}
