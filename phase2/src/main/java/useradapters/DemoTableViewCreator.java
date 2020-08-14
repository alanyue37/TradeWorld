package useradapters;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import tradegateway.TradeModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DemoTableViewCreator extends TableViewCreator {
    public DemoTableViewCreator(TradeModel tm) {
        super(tm);
    }

    @Override
    public void viewAllItems() {
        newItemsToShow();
        getItemsToShow().add(new String[]{"1", "Computer", "Rachel", "Sony Vaio Pentium 3 from 2001"});
        getItemsToShow().add(new String[]{"3", "XBOX", "Bobby", "Comes with 10 games"});
        getItemsToShow().add(new String[]{"5", "Book", "Sarah", "Software Design for Dummies"});
        }

    @Override
    public void viewWishlist() {
        // return empty list
        newItemsToShow();
        getItemsToShow().add(new String[]{"5", "Book", "Sarah", "Software Design for Dummies"});
    }
}
