public class ConfirmTradesPresenter {
    TradeModel tradeModel;

    public ConfirmTradesPresenter(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
    }


    public void startMenu(){
        System.out.println("Confirm trades Menu\n" +
                "Enter 1 to see trades that need to be confirmed\n" +
                "\n Type \"exit\" at any time to exit.");
    }

    public void showTrade(String tradeId){
        System.out.println(tradeModel.getTradeManager().getTradeAllInfo(tradeId));
        System.out.println("Options for a user: \n" +
                " Enter 1 to confirm the real life meeting happened\n" +
                " Enter 2 to skip and confirm later\n" +
                "\n Type \"exit\" at any time to exit.");
    }

    public void end() {
        System.out.println("Exiting...");
    }

    public void invalidInput() {
        System.out.println("Please enter a valid input.");
    }

    public void declineConfirm(){
        System.out.println("Already confirmed. Wait for the other user.");
    }

    public void displayNewDate(String date){
        System.out.println("The next meeting is on " + date + ", 30 days after the first meeting time");
    }
}
