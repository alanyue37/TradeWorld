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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The GUI for a trading user's trade history.
 */
public class TradeHistoryGUI implements RunnableGUI {
    private GridPane root;
    private final Stage stage;
    private final int width;
    private final int height;
    private final TradeModel tradeModel;
    private final String username;
    private final Gson gson;

    /**
     * Creates a new TradeHistoryGUI.
     * @param stage The stage to show the resulting scene on
     * @param width The width of the stage
     * @param height The height of the stage
     * @param model The current TradeModel
     */
    public TradeHistoryGUI(Stage stage, int width, int height, TradeModel model){
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.tradeModel = model;
        this.username = tradeModel.getCurrentUser();
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

        List<String> completed = tradeModel.getTradeManager().getTradesOfUser(username, "completed");
        Map<String, Integer> partners = tradeModel.getTradeManager().userToNumTradesInvolved(completed);
        partners.remove(username);
        List<String> frequentPartners = sortPartnersList(partners); // assumes that sort partners give best partner first
        if (count >= frequentPartners.size()){
            result.getItems().addAll(frequentPartners);
        } else{
            result.getItems().addAll(frequentPartners.subList(0, count));
        }
        return result;
    }

    private List<String> sortPartnersList(Map<String, Integer> partners) {
        List<String> sorted = new ArrayList<>();
        for (String partner : partners.keySet()) {
            if (sorted.size() == 0) {
                sorted.add(partner);
            } else {
                int i = 0;
                while ((i < sorted.size()) && partners.get(partner) < partners.get(sorted.get(i))) {
                    i++;
                }
                sorted.add(i, partner);
            }
        }
        return sorted;
    }
}
