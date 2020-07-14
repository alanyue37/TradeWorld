import java.io.BufferedReader;

public class ProposedTradesPresenter extends TextPresenter {

    public ProposedTradesPresenter() { }

    public void showMeeting(String tradeId, String meetingInfo) {
        System.out.println(meetingInfo);
        System.out.println("Options for a User: \n" +
                " Enter 1 to confirm the meeting time\n" +
                " Enter 2 to edit the meeting time\n" +
                "\n Type \"exit\" at any time to exit.");
    }

    public void editedMeeting() {
        System.out.println("Meeting time edited. Please wait for the other user.");
    }


    public void confirmedMeeting() {
        System.out.println("Meeting time confirmed.");
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

    public void endMeetings() { System.out.println("End of proposed trades."); }
}
