import java.io.BufferedReader;

/**
 * Manages output to the user to confirm or edit the meeting time(s) of proposed trades.
 */
public class ProposedTradesPresenter extends TextPresenter {

    public ProposedTradesPresenter() { }

    /**
     * Prints the options available for the user and asks the user to enter a corresponding number
     * to select an option: confirm the meeting details, edit the meeting details or exit.
     * @param tradeId id of the trade
     * @param tradeInfo information on the trade, including the meeting details
     */
    public void showMeeting(String tradeId, String tradeInfo) {
        System.out.println(tradeInfo);
        System.out.println("Options for a User: \n" +
                " Enter 1 to confirm the meeting time\n" +
                " Enter 2 to edit the meeting time\n" +
                "\n Type \"exit\" at any time to exit.");
    }

    /**
     * Informs the user that they have edited the meeting time and place.
     */
    public void editedMeeting() {
        System.out.println("Meeting time edited. Please wait for the other user.");
    }

    /**
     * Informs the user that they have accepted the meeting time and place.
     */
    public void confirmedMeeting() {
        System.out.println("Meeting time confirmed.");
    }

    /**
     * Informs the user that they have already given an suggestion, waiting for the other party to confirm/decline.
     */
    public void declineConfirmMeeting() {
        System.out.println("User must wait for the other party to confirm the meeting.");
    }

    /**
     * Asks the user to enter their suggestion of meeting location.
     */
    public void enterLocation() {
        System.out.println("Enter the location of the meeting: ");
    }

    /**
     * Asks the user to enter their suggestion of meeting time.
     */
    public void enterDateTime() {
        System.out.println("Enter the date and time of the meeting (dd/mm/yyyy hh:mm): ");
    }

    /**
     * Informs the user that they entered an invalid time.
     */
    public void invalidDateTime() {
        System.out.println("Enter a valid date and time!");
    }

    /**
     * Informs the user that they cannot edit, waiting for the other party to confirm/decline.
     */
    public void declineEditMeeting() {
        System.out.println("User must wait for the other party to edit the meeting.");
    }

    /**
     * Informs the user that the trade was cancelled.
     */
    public void canceledTrade() {
        System.out.println("The trade was cancelled.");
    }

    /**
     * Prints to the screen and signals to the user that the system has successfully ended.
     */
    public void end() {
        System.out.println("Exiting...");
    }

    /**
     * Informs the user that they are at the end of proposed trades to confirm.
     */
    public void endMeetings() { System.out.println("No more proposed trades."); }
}
