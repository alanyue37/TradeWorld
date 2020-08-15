package demoadapters;


import javafx.scene.control.*;

import javafx.stage.Stage;
import tradeadapters.TradeGUI;
import tradegateway.TradeModel;
import trademain.RunnableGUI;

public class DemoTradeGUI extends TradeGUI implements RunnableGUI {

    public DemoTradeGUI(Stage stage, int width, int height, TradeModel tradeModel) {
        super(stage, width, height, tradeModel);
    }

    @Override
    protected void configureInitiateButtons(Button oneWayTemporary, Button oneWayPermanent, Button twoWayPermanent, Button twoWayTemporary, ScrollPane scrollPane, Label messageBox){
        oneWayTemporary.setOnAction(actionEvent -> messageBox.setText("One way temporary trade would be created."));
        oneWayPermanent.setOnAction(actionEvent -> messageBox.setText("One way permanent trade would be created."));
        twoWayPermanent.setOnAction(actionEvent -> messageBox.setText("Two way permanent trade would be created."));
        twoWayTemporary.setOnAction(actionEvent -> messageBox.setText("Two way temporary trade would be created."));
        scrollPane.setDisable(true);
    }

    @Override
    protected void configureProposedTradesButtons(Button confirmBtn, Button editBtn, Button declineBtn, Label messageBox, ListView<String> proposedTradesListView){
        confirmBtn.setOnAction(actionEvent -> messageBox.setText("The meeting time of the selected trade would be confirmed."));
        editBtn.setOnAction(actionEvent -> messageBox.setText("You would be allowed to edit your meeting time."));
        declineBtn.setOnAction(actionEvent -> messageBox.setText("The selected proposed trade would be deleted."));
        proposedTradesListView.setPlaceholder(new Label("The proposed trades would be here."));
    }

    @Override
    protected void configureConfirmTradesButtons(Button confirmBtn, Label messageBox, ListView<String> confirmTradesListView){
        confirmBtn.setOnAction(actionEvent -> messageBox.setText("The selected trade would be confirmed. "));
        confirmTradesListView.setPlaceholder(new Label("The trades that need to be confirmed would be here."));
    }

    @Override
    protected void configureViewButtons(Button addReview, Label messageBox, TextField ratingInput, TextField tradeIdInput, TextField commentInput){
        addReview.setOnAction(actionEvent -> messageBox.setText("The review would be added."));
        ratingInput.setPromptText("You would enter rating here.");
        tradeIdInput.setPromptText("You would enter trade Id here.");
        commentInput.setPromptText("You would enter comment here");
    }
}