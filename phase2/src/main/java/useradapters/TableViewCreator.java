package useradapters;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableViewCreator {

    private final UserPresenter presenter;
    private String[] colTitles;
    private double[] widths;

    public TableViewCreator(UserPresenter presenter) {
        this.presenter = presenter;
    }

    public TableView<ObservableList<String>> create(String type) {

        switch(type) {
            case "own inventory":
                presenter.viewInventory();
                colTitles = new String[]{"ID", "Name", "Description", "Approved"};
                widths = new double[]{0.1, 0.3, 0.5, 0.1};
                break;
            case "all items":
                presenter.viewAllItems();
                colTitles = new String[]{"ID", "Name", "Owner", "Description"};
                widths = new double[]{0.1, 0.2, 0.2, 0.5};
                break;
            case "wishlist":
                presenter.viewWishlist();
                colTitles = new String[]{"ID", "Name", "Owner", "Description"};
                widths = new double[]{0.1, 0.2, 0.2, 0.5};
                break;
        }

        TableView<ObservableList<String>> table = new TableView<>();
        ObservableList<ObservableList<String>> observableItemList = FXCollections.observableArrayList();
        while (presenter.hasNext()) {
            String[] tokens = new String[colTitles.length];
            for (int i = 0; i < colTitles.length; i++) {
                tokens[i] = presenter.next();
            }
            observableItemList.add(FXCollections.observableArrayList(tokens));
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
