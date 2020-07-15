public class ConfirmTradesPresenter extends TextPresenter {

    public ConfirmTradesPresenter() { // Java already has a default constructor
    }

    public void showTrade(String tradeId, String tradeInfo){
        System.out.println(tradeInfo);
        System.out.println("Options for a user: \n" +
                " Enter 1 to confirm the real life trade happened\n" +
                " Enter 2 to skip and confirm later\n" +
                "\n Type \"exit\" at any time to exit.");
    }

    public void end() {
        System.out.println("Exiting...");
    }

    public void declineConfirm(){
        System.out.println("Already confirmed. Please wait for the other user.");
    }

    public void displayNewDate(String date){
        System.out.println("The next meeting is on " + date + ", 30 days after the first meeting time.");
    }

    public void confirmedTrade() {
        System.out.println("Trade confirmed.");
    }

    public void endTrades() { System.out.println("End of trades."); }

}
