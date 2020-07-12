import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UserController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    UserPresenter presenter;
    private String username;
    // private Scanner sc;

    /**
     * Constructor for UserController.
     * @param tradeModel Model of the system.
     * @param username Username associated with the user interacting with the system.
     */
    public UserController(TradeModel tradeModel, String username){
        br = new BufferedReader(new InputStreamReader(System.in));
        // Scanner sc = new Scanner(System.in);
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new UserPresenter(tradeModel);
    }

    /**
     * Main method to run the UserController
     */
    public void run() {
        try {
            if(!selectMenu()) {
                System.out.println("Please enter a valid input.");
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
        presenter.startMenu();
        String input = br.readLine();
        // String input = sc.nextLine();
        switch(input) {
            case "1":
                viewMenu();
                break;
            case "2":
                tradeOptions();
                break;
            case "exit":
                return false;
            default:
                System.out.println("Please enter a valid input.");
        }
        return true;
    }

    /**
     * Menu to run the view options for UserController
     */
    public void viewMenu() throws IOException {
        presenter.printViewOptions();
        String viewInput = br.readLine();
        // String viewInput = sc.nextLine();
        switch (viewInput){
            case "1":
                viewInventory();
                break;
            case "2":
                viewWishlist();
                break;
            case "3":
                viewLastThreeTrades();
                break;
            case "4":
                viewTradingPartners();
                break;
            case "exit":
                break;
            default:
                System.out.println("Please enter a valid input.");
        }
    }

    /**
     * Options depending on whether the user account is frozen or not
     */
    public void tradeOptions() throws IOException {
        if (tradeModel.getUserManager().isFrozen(username)){
            tradeUserAccountIsFrozen();
        }
        else {
            tradeMenu();
        }
    }

    /**
     * View the Inventory. Gives the user the option of adding items to their wishlist
     */
    public void viewInventory() throws IOException{
        presenter.printSystemInventory();
        presenter.printUserInventoryOptions();
        String choice = br.readLine();
        // String choice = sc.nextLine();
        if (choice.equals("1")){
            presenter.printInputItemID();
            String itemId = br.readLine();
            // String itemId = sc.nextLine();
            tradeModel.getUserManager().addToSet(username, itemId, ItemSets.WISHLIST);
        }
    }

    /**
     * View user wishlist
     */
    public void viewWishlist(){
        presenter.printUserWishlist(username);
    }

    /**
     * View top 3 most frequent trading partners
     */
    public void viewTradingPartners(){
        presenter.printViewTopTradingPartner(tradeModel.getTradeManager().getFrequentPartners(3,username));
    }

    /**
     * View user's last 3 trades
     */
    public void viewLastThreeTrades(){
        presenter.viewLastThreeTrades(username);
    }

    /**
     * Tells the user that their account is frozen and shows option for user to unfreeze their account
     */
    public void tradeUserAccountIsFrozen() throws IOException{
        presenter.printAccountIsFrozenOption();
        String viewInput = br.readLine();
        // String viewInput = sc.nextLine();
        if (viewInput.equals("1")){
            tradeModel.getUserManager().markUserForUnfreezing(username);
        }
    }

    /**
     * Menu to run the trade options for UserController
     */
    public void tradeMenu()throws IOException{
        presenter.printViewTradeOptions();
        String viewInput = br.readLine();
        // String viewInput = sc.nextLine();
        switch (viewInput) {
            case "1":
                tradeLendItem();
                break;
            case "2":
                tradeBorrowTemporary();
                break;
            case "3":
                tradeBorrowPermanent();
                break;
            case "4":
                tradeTwoWayTemporary();
                break;
            case "5":
                tradeTwoWayPermanent();
                break;
            case "exit":
                break;
            default:
                System.out.println("Please enter a valid input.");

        }

    }

    /**
     * Allows user to create an item and lend it
     */
    public void tradeLendItem()throws IOException{
        presenter.printInputItemName();             // enter name of the item
        String itemName = br.readLine();
        // String itemName = sc.nextLine();
        presenter.printInputItemDescription();      // enter description of the item
        String itemDescription = br.readLine();
        // String itemDescription = sc.nextLine();
        tradeModel.getItemManager().addItem(itemName, username, itemDescription);
    }

    /**
     * Allows user to temporarily borrow an item
     */
    public void tradeBorrowTemporary()throws IOException{
        presenter.printSystemInventory();
        presenter.printInputItemID();
        String itemIdTemporary = br.readLine();
        // String itemId = sc.nextLine();
        if (tradeModel.getUserManager().getUsersForFreezing().contains(username)){
            System.out.println("You have lent more items than you have borrowed");
        }
        else{
            String tradeId = tradeModel.getTradeManager().addOneWayTrade("temporary", tradeModel.getItemManager().getOwner(itemIdTemporary), username, itemIdTemporary);
            presenter.printUserWishlist(br.readLine());
            // presenter.printUserWishlist(sc.nextLine());

            System.out.println("Enter the location of the meeting: ");
            String location = br.readLine();
            System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
            String dateString = br.readLine();

            Date convertedDate = null;

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
            try {
                convertedDate = format.parse(dateString);
            } catch (ParseException e) {
                System.out.println("Enter a valid date and time!");
            }
            tradeModel.getTradeManager().addMeetingToTrade(tradeId, location, convertedDate);
        }
    }

    /**
     * Allows user to permanently borrow an item
     */
    public void tradeBorrowPermanent()throws IOException{
        presenter.printSystemInventory();
        presenter.printInputItemID();
        String itemIdPermanent = br.readLine();
        // String itemId = sc.nextLine();

        if (tradeModel.getUserManager().getUsersForFreezing().contains(username)){
            System.out.println("You have lent more items than you have borrowed");
        }
        else{
            String tradeId = tradeModel.getTradeManager().addOneWayTrade("permanent", tradeModel.getItemManager().getOwner(itemIdPermanent), username, itemIdPermanent);
            presenter.printUserWishlist(br.readLine());
            // presenter.printUserWishlist(sc.nextLine());

            // String tradeId, String location, Date time
            System.out.println("Enter the location of the meeting: ");
            String location = br.readLine();
            System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
            String dateString = br.readLine();

            Date convertedDate = null;

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
            try {
                convertedDate = format.parse(dateString);
            } catch (ParseException e) {
                System.out.println("Enter a valid date and time!");
            }
            tradeModel.getTradeManager().addMeetingToTrade(tradeId, location, convertedDate);
        }
    }

    /**
     * Allows user to propose a temporary two-way trade
     */
    public void tradeTwoWayTemporary()throws IOException{
        presenter.printSystemInventory();
        presenter.printInputItemID();
        String itemId1 = br.readLine();
        // String itemId = sc.nextLine();

        presenter.printInputItemID();
        presenter.printUserInventory(username);
        presenter.printInputItemID();
        String itemId2 = br.readLine();

        String tradeId = tradeModel.getTradeManager().addTwoWayTrade("temporary", tradeModel.getItemManager().getOwner(itemId1), username, itemId1, itemId2);

        presenter.printEnterTradeLocation();
        String location = br.readLine();
        presenter.printEnterTradeDate();
        String dateString = br.readLine();

        Date convertedDate = null;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
        try {
            convertedDate = format.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Enter a valid date and time!");
        }
        tradeModel.getTradeManager().addMeetingToTrade(tradeId, location, convertedDate);

    }

    /**
     * Allows user to propose a permanent two-way trade
     */
    public void tradeTwoWayPermanent()throws IOException{
        presenter.printSystemInventory();
        presenter.printInputItemID();
        String itemId1 = br.readLine();
        // String itemId = sc.nextLine();

        presenter.printInputItemID();
        presenter.printUserInventory(username);
        presenter.printInputItemID();
        String itemId2 = br.readLine();

        String tradeId = tradeModel.getTradeManager().addTwoWayTrade("permanent", tradeModel.getItemManager().getOwner(itemId1), username, itemId1, itemId2);

        presenter.printEnterTradeLocation();
        String location = br.readLine();
        presenter.printEnterTradeDate();
        String dateString = br.readLine();

        Date convertedDate = null;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
        try {
            convertedDate = format.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Enter a valid date and time!");
        }
        tradeModel.getTradeManager().addMeetingToTrade(tradeId, location, convertedDate);

    }

}