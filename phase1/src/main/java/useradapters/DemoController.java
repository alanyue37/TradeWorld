package useradapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;

import java.io.IOException;
import java.util.List;

public class DemoController extends UserController implements RunnableController{

    /**
     * Constructor for DemoController.
     * @param tradeModel Model of the system.
     * @param username Username associated with the user interacting with the system.
     */
    public DemoController(TradeModel tradeModel, String username){
        super(tradeModel, username);
    }

    /**
     * trademisc.Main menu to run the DemoController
     */
    @Override
    protected boolean selectMenu() throws IOException {
        presenter.startMenu(username);
        boolean validInput = false;
        do {
            String input = br.readLine();
            switch (input) {
                case "1": // add items to inventory
                    createItem();
                    validInput = true;
                    break;
                case "2": // add items to wishlist
                    viewItemsToAddToWishlist();
                    validInput = true;
                    break;
                case "3": // view inventory, wishlist
                case "4": // view frequent trades, trading history, trade status
                case "5": // initiate a trade
                case "6": // manage proposed trades
                case "7": // confirm a trade
                    presenter.printDemoMessage();
                    break;
                case "exit":
                    presenter.end();
                    return false;
                default:
                    presenter.tryAgain();
            }
        } while (!validInput);
        return true;
    }

    /**
     * Allows user to simulate creating an item
     */
    @Override
    protected void createItem(){
        presenter.printInputItemName();             // enter name of the item
        presenter.printInputItemDescription();      // enter description of the item
        presenter.printDemoMessage();
    }

    /**
     * View system inventory. Gives the user the option of simulating adding items to their wishlist.
     *
     * @throws IOException if a problem occurs with reading in input
     */
    @Override
    public void viewItemsToAddToWishlist() throws IOException {
        List<String> items = tradeModel.getItemManager().getConfirmedItems();
        presenter.printItemsToAddToWishlist(getItemsInfo(items));
        String choice = br.readLine();
        while (!items.contains(choice) && !choice.equals("back")) {
            presenter.tryAgain();
            choice = br.readLine();
        }
        if (items.contains(choice)) {
            presenter.printDemoMessage();
        }
    }

}