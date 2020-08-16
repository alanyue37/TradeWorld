package demoadapters;

import tradegateway.TradeModel;
import useradapters.TableViewCreator;

/**
 * Factory responsible for the creation of TableView tables used in the DemoGUIs.
 */
public class DemoTableViewCreator extends TableViewCreator {

    /**
     * Instantiates a new DemoTableViewCreator.
     *
     * @param tm The current TradeModel
     */
    public DemoTableViewCreator(TradeModel tm) {
        super(tm);
    }

    /**
     * Sets itemsToShow to a predefined list of items corresponding to a user's inventory for demo purposes.
     */
    @Override
    protected void viewInventory() {
        newItemsToShow();
        getItemsToShow().add(new String[]{"1", "Book", "Software Design for Dummies", "Yes"});
        getItemsToShow().add(new String[]{"5", "iPad", "BRAND NEW receipt unavailable", "Yes"});
        getItemsToShow().add(new String[]{"7", "Fork", "Still Shiny", "Yes"});
    }

    /**
     * Sets itemsToShow to a predefined list of items corresponding to all items a user could trade for demo purposes.
     */
    @Override
    protected void viewAllItems() {
        newItemsToShow();
        getItemsToShow().add(new String[]{"2", "Desk", "Rachel", "Finished my 207 project on this"});
        getItemsToShow().add(new String[]{"3", "Spoon", "Ayushi", "Will only trade for a fork"});
        getItemsToShow().add(new String[]{"4", "Computer", "Bob", "Sony Vaio Pentium 3"});
        getItemsToShow().add(new String[]{"6", "XBOX", "Sarah", "Comes with 10 games"});
        }

    /**
     * Sets itemsToShow to a predefined list of items corresponding to a user's wishlist for demo purposes.
     */
    @Override
    protected void viewWishlist() {
        newItemsToShow();
        getItemsToShow().add(new String[]{"3", "Spoon", "Ayushi", "Will only trade for a fork"});
    }
}