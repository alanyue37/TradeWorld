import java.io.BufferedReader;

public class ProposedTradesPresenter {
    TradeModel tradeModel;

    public ProposedTradesPresenter(TradeModel tradeModel) {
        this.tradeModel = tradeModel;
    }

    public void startMenu() {
        System.out.println("Enter 1 to see trades proposed to you: ");
    }

    public void invalidInput() {
        System.out.println("Please enter a valid input.");
    }

    public void showMeeting(String tradeId) {
        System.out.println(tradeModel.getTradeManager().getTradeMeetingInfo(tradeId));
        System.out.println("Options for a User: \n" +
                " Enter 1 to confirm the meeting time\n" +
                " Enter 2 to edit the meeting time\n" +
                "\n Type \"exit\" at any time to exit.");
    }

    public void declineConfirmMeeting() {
        System.out.println("User must wait for the other party to confirm the meeting.");
    }

    public void enterLocation() {
        System.out.println("Enter the location of the meeting: ");
    }

    public void enterDateTime() {
        System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
    }

    public void invalidDateTime() {
        System.out.println("Enter a valid date and time!");
    }

    public void declineEditMeeting() {
        System.out.println("User must wait for the other party to edit the meeting.");
    }

    public void canceledTrade() {
        System.out.println("The trade was cancelled.");
    }

    public void end() {
        System.out.println("Exiting...");
    }

}
