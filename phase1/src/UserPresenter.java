import java.util.ArrayList;

public class UserPresenter {
    TradeModel tradeModel;

    public UserPresenter(TradeModel tradeModel){
        this.tradeModel = tradeModel;
    }

    public void startMenu() {
        System.out.println("User Options\n" +
                " Enter 1 to view Inventory\n" +
                " Enter 2 to view Wishlist\n " +
                " Enter 3 to chose an item for trading from the Inventory\n " +
                " Enter 4 to create an item for Trading\n " +
                " Enter 5 to view look for items to add to your wish list\n" +
                " Enter 6 to request your account to be unfrozen\n" +
                " Enter 7 to see the last three trades\n" +
                " Enter 8 to request a trade\n" +
                "\n Type \"exit\" at any time to exit.");
    }

    public void printViewOptions() {
        System.out.println("View Options\n" +
                " Enter 1 to view inventory\n" +
                " Enter 2 to view wishlist\n " +
                " Enter 3 to view last transaction \n " +
                " Enter 4 to view top 3 most frequent trading partners: \n" +
                "\n Type \"exit\" at any time to exit.");
    }

    public void printViewTradeOptions() {
        System.out.println("Trade Options\n" +
                " Enter 1 to lend items\n" +
                " Enter 2 to borrow items\n " +
                " Enter 3 to trade items \n " +
                "\n Type \"exit\" at any time to exit.");
    }


    public void printInputUserName(){
        System.out.println("Enter your name: ");
    }

    public void printInputUserEmail(){
        System.out.println("Enter your email: ");
    }

    public void printInputUserId(){
        System.out.println("Enter your User Id: ");
    }

    public void printInputItemName(){
        System.out.println("Enter Item's name: ");
    }

    public void printInputItemDescription(){
        System.out.println("Enter Item's description: ");
    }

    public void printItemAddingFailure(){
        System.out.println("Sorry! The item could not be added");
    }

    public void printUserInventory(String username){
        System.out.println("Inventory: " + tradeModel.getUserManager().getSetByUsername(username, itemSets.INVENTORY));
    }

    public void printUserWishlist(String username){
        System.out.println("Wishlist: " + tradeModel.getUserManager().getSetByUsername(username, itemSets.WISHLIST));
    }

    public void printSystemInventory(){
        System.out.println("System Inventory: " + tradeModel.getItemManager().getAvailableItems());
    }

    public void printInputItemID(){
        System.out.println("Enter Item's id: ");
    }

    public void printUserInventoryOptions(){
        System.out.println("Enter 1 to view Inventory\n or enter \"exit\" \n ");
    }

    public void printViewTopTradingPartner(ArrayList<String> partners){
        System.out.println("Your top 3 trading partners are: " + partners);
    }

    public void printAccountIsFrozenOption(){
        System.out.println("Sorry your account is frozen\n" +
                " Enter 1 to request your account to be unfrozen \n" +
                " Enter \"exit\" to exit \n");
    }

}
