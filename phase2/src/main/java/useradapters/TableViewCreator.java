package useradapters;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.List;

public class TableViewCreator {

    private final UserPresenter2 presenter;
    private List<String[]> data;
    private String[] colTitles;
    private double[] widths;

    public TableViewCreator(UserPresenter2 presenter) {
        this.presenter = presenter;
    }

    public TableView<ObservableList<String>> create(String type) throws IOException {

        switch(type) {
            case "own inventory":
                data = presenter.viewInventory();
                colTitles = new String[]{"ID", "Name", "Description"};
                widths = new double[]{0.1, 0.3, 0.6};
                break;
            case "all items":
                data = presenter.viewAllItems();
                colTitles = new String[]{"ID", "Name", "Owner", "Description"};
                widths = new double[]{0.1, 0.2, 0.2, 0.5};
                break;
            case "wishlist":
                data = presenter.viewWishlist();
                colTitles = new String[]{"ID", "Name", "Owner", "Descrpition"};
                widths = new double[]{0.1, 0.2, 0.2, 0.5};
                break;
        }

        TableView<ObservableList<String>> table = new TableView<>();
        ObservableList<ObservableList<String>> observableItemList = FXCollections.observableArrayList();
        for (String[] row : data) {
            observableItemList.add(FXCollections.observableArrayList(row));
        }
        table.setItems(observableItemList);

        for (int i = 0; i < colTitles.length; i++) {
            final int colNum = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(colTitles[colNum]);
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(colNum))
            );
            table.getColumns().add(column);
            column.prefWidthProperty().bind(table.widthProperty().multiply(widths[colNum]));
        }

        return table;
    }
}
