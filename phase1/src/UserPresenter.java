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

    public void printInputItemName(){
        System.out.println("Enter name of the Item: ");
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

    public void printInputItemDescription(){
        System.out.println("Enter Item's description: ");
    }

    public void printUserInventory(String email){
        System.out.println("Inventory" + tradeModel.getUserManager().getInventory(email));
    }

    public void printUserWishlist(String email){
        System.out.println("Wishlist" + tradeModel.getUserManager().getWishlist(email));
    }

    public void printAvailableItems(){
        System.out.println("Inventory" + tradeModel.getItemManager().getAvailableItems());
    }



}
