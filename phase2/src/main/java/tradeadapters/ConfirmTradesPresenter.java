package tradeadapters;

import org.json.JSONObject;
import trademisc.TextPresenter;

import java.util.List;

/**
 * Manages output to the user to confirm trades happened in real life.
 */
public class ConfirmTradesPresenter extends TextPresenter {

    /**
     * Prints the options available for the user and asks the user to enter a corresponding number
     * to select an option: confirm real life meeting, skip to next one or exit.
     *
     * @param tradeInfo information on the trade, including the meeting details
     */
    public void showTrade(String tradeInfo) {
        System.out.println(tradeInfo);
        System.out.println("Options for a user: \n" +
                " Enter 1 to confirm the real life trade happened\n" +
                " Enter 2 to skip and confirm later\n" +
                "\n Type \"exit\" at any time to exit.");
    }

    /**
     * Prints to the screen and signals to the user that the system has successfully ended.
     */
    public void end() {
        System.out.println("Exiting...");
    }

    /**
     * Informs the user that they have already confirmed the meeting and is waiting an answer from the other user.
     */
    public void declineConfirm(){
        System.out.println("Already confirmed. Please wait for the other user.");
    }

    /**
     * Displays the new return meeting date.
     * @param date date of returning meeting (for temporary trade)
     */
    public void displayNewDate(String date){
        System.out.println("The next meeting is on " + date + ", 30 days after the first meeting time.");
    }

    /**
     * Informs the user that they have successfully confirmed that the meeting happened in real life.
     */
    public void confirmedTrade() {
        System.out.println("Trade confirmed.");
    }

    /**
     * Informs the user that they are at the end of their trades to confirm.
     */
    public void endTrades() { System.out.println("No more trades to confirm."); }

    public void askRating() { System.out.println("If you would like to leave a review of your trading experience," +
            "please enter a rating on a scale of 1-5. If not, press enter."); }

    public void invalidRating() { System.out.println("Invalid rating. Please enter a rating on a scale of 1-5."); }

    public void askComment() { System.out.println("If you would like to leave a comment," +
            "please enter it here. If not, press enter. If you would like to cancel your review, enter \"exit\"."); }

}
