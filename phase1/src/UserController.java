import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class UserController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    UserPresenter presenter;
    private String username;
    private Scanner sc;

    public UserController(TradeModel tradeModel, String username){
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new UserPresenter(tradeModel);
        Scanner sc = new Scanner(System.in);
    }

    public void run() {
        try {
            if(!selectMenu()) {
                System.out.println("Please enter a valid input.");
            }
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private boolean selectMenu() throws IOException {
        presenter.startMenu();
        String input = br.readLine();
        // String input = sc.nextLine();
        switch(input) {
            case "1":
                viewMenu();
                break;
            case "2":
                tradeMenu();
                break;
            case "exit":
                return false;
            default:
                System.out.println("Please enter a valid input.");
        }
        return true;
    }

    public void viewMenu() throws IOException {
        presenter.printViewOptions();
        String viewInput = br.readLine();
        // String viewInput = sc.nextLine();
        switch (viewInput){
            case "1":
                // view inventory
                presenter.printSystemInventory();
                presenter.printUserInventoryOptions();
                String choice = br.readLine();
                // String choice = sc.nextLine();
                if (choice.equals("1")){
                    presenter.printInputItemID();
                    String itemId = br.readLine();
                    // String itemId = sc.nextLine();
                    tradeModel.getUserManager().addToSet(username, itemId, itemSets.WISHLIST);
                }
                break;
            case "2":
                // view wishlist
                presenter.printUserWishlist(username);
                break;
            case "3":
                // view last transaction
                // TODO: Get last trade of a user from UserManager
                break;
            case "4":
                // view top 3 most frequent trading partners
                presenter.printViewTopTradingPartner(tradeModel.getTradeManager().getFrequentPartners(3,username););
                break;
            case "exit":
                break;
            default:
                System.out.println("Please enter a valid input.");
        }
    }

    public void tradeMenu() throws IOException {
        if (tradeModel.getUserManager().isFrozen(username)){
            presenter.printAccountIsFrozenOption();
            String viewInput = br.readLine();
            // String viewInput = sc.nextLine();
            if (viewInput.equals("1")){
                tradeModel.getUserManager().markUserForUnfreezing(username);
            }
        }
        else {


            presenter.printViewTradeOptions();
            String viewInput = br.readLine();
            // String viewInput = sc.nextLine();
            switch (viewInput) {
                case "1":
                    // Lend item

                    // create an item here
                    presenter.printInputItemName();             // enter name of the item
                    String itemName = br.readLine();
                    // String itemName = sc.nextLine();
                    presenter.printInputItemDescription();      // enter description of the item
                    String itemDescription = br.readLine();
                    // String itemDescription = sc.nextLine();
                    tradeModel.getItemManager().addItem(itemName, username, itemDescription);

                    break;
                case "2":
                    // Borrow an item here (temporary)

                    presenter.printUserInventory(username);     // print inventory
                    presenter.printInputItemID();
                    String itemIdTemporary = br.readLine();
                    // String itemId = sc.nextLine();
                    tradeModel.getTradeManager().addOnewayTrade("temporary", tradeModel.getItemManager().getOwner(itemIdTemporary), username, itemIdTemporary);
                    presenter.printInputUserEmail();
                    presenter.printUserWishlist(br.readLine());
                    // presenter.printUserWishlist(sc.nextLine());
                    break;

                case "3":
                    // Borrow an item here (Permanent)

                    presenter.printUserInventory(username);     // print inventory
                    presenter.printInputItemID();
                    String itemIdPermanent = br.readLine();
                    // String itemId = sc.nextLine();
                    tradeModel.getTradeManager().addOnewayTrade("permanent", tradeModel.getItemManager().getOwner(itemIdPermanent), username, itemIdPermanent);
                    presenter.printInputUserEmail();
                    presenter.printUserWishlist(br.readLine());
                    // presenter.printUserWishlist(sc.nextLine());
                    break;

                case "4":
                    // Two way product

                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Please enter a valid input.");

            }
        }
    }


}