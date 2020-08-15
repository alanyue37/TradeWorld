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
    protected void viewInventory() {
        newItemsToShow();
        getItemsToShow().add(new String[]{"1", "Book", "Software Design for Dummies", "Yes"});
        getItemsToShow().add(new String[]{"5", "iPad", "BRAND NEW receipt unavailable", "Yes"});
        getItemsToShow().add(new String[]{"7", "Fork", "Still Shiny", "Yes"});
    }

    @Override
    protected void viewAllItems() {
        newItemsToShow();
        getItemsToShow().add(new String[]{"2", "Desk", "Rachel", "Finished my 207 project on this"});
        getItemsToShow().add(new String[]{"3", "Spoon", "Ayushi", "Will only trade for a fork"});
        getItemsToShow().add(new String[]{"4", "Computer", "Bobby", "Sony Vaio Pentium 3"});
        getItemsToShow().add(new String[]{"6", "XBOX", "Sarah", "Comes with 10 games"});
        }

    @Override
    protected void viewWishlist() {
        // return empty list
        newItemsToShow();
        getItemsToShow().add(new String[]{"3", "Spoon", "Ayushi", "Will only trade for a fork"});
    }
}
