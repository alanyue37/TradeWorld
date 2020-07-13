import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UserController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    UserPresenter presenter;
    private String username;

    /**
     * Constructor for UserController.
     * @param tradeModel Model of the system.
     * @param username Username associated with the user interacting with the system.
     */
    public UserController(TradeModel tradeModel, String username){
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new UserPresenter();
    }

    /**
     * Main method to run the UserController
     */
    public void run() {
        try {
            presenter.startMenu(); // should this run method have more than 1 while loop (i.e., one for viewing and the other for trade)?
            String input = br.readLine();
            while (!input.equals("exit")) {
                selectMenu();
            }
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    /**
     * Main menu to run the UserController
     * There are two options that are available (viewing and trading)
     */
    private boolean selectMenu() throws IOException {
        // public void startMenu() {
        //        System.out.println("User Options\n" +
        //                " Enter 1 to add items to inventory\n" +
        //                " Enter 2 to add items to inventory\n" +
        //                " Enter 3 to view menu\n" +
        //                " Enter 4 to initiate a trade\n" +
        //                " Enter 5 to propose a trade\n" +
        //                " Enter 6 to confirm a trade\n" +
        //                "\n Type \"exit\" at any time to exit.");
        //    }
        presenter.startMenu();
        String input = br.readLine();
        switch(input) {
            case "1": // add items to inventory
                createItem();
                break;
            case "2": // add items to wishlist
                viewInventory();
                break;
            case "3": // view menu
                ViewingMenuController vmc = new ViewingMenuController(tradeModel, username);
                vmc.run();
                break;
            case "4": // initiate a trade
                // tradeOptions();
                break;
            case "5": // propose a trade
                ProposedTradesController ptc = new ProposedTradesController(tradeModel, username);
                ptc.run();
                break;
            case "6": //confirm a trade
                ConfirmTradesController ctc = new ConfirmTradesController(tradeModel, username);
                ctc.run();
                break;
            case "exit":
                return false;
            default:
                System.out.println("Please enter a valid input.");
        }
        return true;
    }

    /**
     * Allows user to create an item
     */
    private void createItem()throws IOException{
        presenter.printInputItemName();             // enter name of the item
        String itemName = br.readLine();
        presenter.printInputItemDescription();      // enter description of the item
        String itemDescription = br.readLine();
        tradeModel.getItemManager().addItem(itemName, username, itemDescription);
    }

    /**
     * View the Inventory. Gives the user the option of adding items to their wishlist
     */
    public void viewInventory() throws IOException{
        presenter.printInventory(tradeModel, username);
        presenter.printUserInventoryOptions();
        String choice = br.readLine();
        if (choice.equals("1")){
            presenter.printInputItemID();
            String itemId = br.readLine();
            tradeModel.getUserManager().addToSet(username, itemId, ItemSets.WISHLIST);
        }
    }
}