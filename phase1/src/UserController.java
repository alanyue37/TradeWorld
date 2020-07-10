import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class UserController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    UserPresenter presenter;
//    private String username;

    public UserController(TradeModel tradeModel){
        br = new BufferedReader(new InputStreamReader(System.in));
        presenter = new UserPresenter(tradeModel);
        this.tradeModel = tradeModel;
    }

//    The new constructor take takes in username as well
//    public UserController(TradeModel tradeModel, String username){
//        br = new BufferedReader(new InputStreamReader(System.in));
//        this.tradeModel = tradeModel;
//        this.username = username;
//        presenter = new UserPresenter(tradeModel);
//    }

    public void run() {
        try {
            selectMenu(); // TODO: I didn't understand why it was returning null before. Are you trying to loop?
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private boolean selectMenu() throws IOException {
        presenter.startMenu();
        String input = br.readLine();
        switch(input) {
            case "1":
                presenter.printInputUserEmail();
                presenter.printUserInventory(br.readLine());
                break;
            case "2":
                presenter.printInputUserEmail();
                presenter.printUserWishlist(br.readLine());
                break;
            case "3":
                presenter.printAvailableItems();
                String a = br.readLine();

                break;
            case "4":
                presenter.printInputItemName();
                String name = br.readLine();
                presenter.printInputUserName();
                String owner = br.readLine();
                presenter.printInputItemDescription();
                String description = br.readLine();
                tradeModel.getItemManager().addItem(name, owner, description);
                break;
            case "5":
                presenter.printAvailableItems();
                presenter.printInputItemName();
                String itemName = br.readLine();
                tradeModel.getUserManager().addToWishlist(tradeModel.getItemManager().getConfirmedItem(itemName));
                //I'm not sure which user's wishlist this item is being added to.
                break;
            case "6":
                presenter.printInputUserEmail();
                String email = br.readLine();
                tradeModel.getUserManager().freeze(email, true);
                break;
            case "7":
                presenter.printInputUserId();
                String userId = br.readLine();
                tradeModel.getUserManager().getLastThreeTrades(userId); //I think this is a method for TradeManager?
                break;
            case "8":
                System.out.println("Request a trade");
                break;
            case "exit":
                return false;
            default:
                System.out.println("Please enter a valid input.");
        }
        return true;
    }


}