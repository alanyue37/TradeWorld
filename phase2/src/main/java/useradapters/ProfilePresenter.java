package useradapters;

import trademisc.TextPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProfilePresenter extends TextPresenter {

    public void showOptions(){
        List<String> options = new ArrayList<>();
        options.add("Turn on/off your vacation mode");
        options.add("Account privacy");
        options.add("Manage friend requests");
        options.add("Send friend requests");
        options.add("View your friends list");
        options.add("View your account settings");
        options.add("Add review to past trade");

        printList(options, true, false);

        System.out.println("\nPlease enter the # of your choice or \"exit\" to exit: ");
    }

    public void accountModeSelection() {
        System.out.println("Enter 1 to set to vacation mode and 0 to unset vacation mode.");
    }

    public void invalidInput() {
        System.out.println("This is an invalid input.\n Please try again!");
    }

    public void accountPrivacySelection(){
        System.out.println("Enter 1 to set account to private and 0 to set to public.");
    }

    public void manageRequest(String user) {
        System.out.println(user + " has sent you a friend request. Enter 1 to accept his friend request, 2 to " +
                "decline or 0 to skip.");
    }

    public void printEnterFriendUsername(){
        System.out.println("Enter username of friend to add them");
    }

    public void printSearchingInvalid(){
        System.out.println("No user with this username.");
    }

    public void addedFriend(){
        System.out.println("Friend requests sent. ");
    }

    public void alreadyFriend(){
        System.out.println("Already friends with this user.");
    }

    public void alreadySentRequest(){
        System.out.println("Already sent out a friend request. Please wait for the other user's response");
    }

    public void printViewAccountSettings(String privacy, String vacation, String city){
        System.out.println("Your account current status is: \n" + "Privacy: " + privacy + "\nVacation Mode: " +
                vacation + "\nCity: " + city);
    }

    public void printViewFriends(List<String> friends){
        printList(friends, true, false);
    }

    public void printEnterTradeIdForReview(){
        System.out.println("Enter the trade Id of the trade that you would like to leave a review.");
    }

    public void printInvalidTradeId(){
        System.out.println("You do not have any completed trade with this ID.");
    }

    public void alreadyWroteReview(String receiver, String tradeId){
        System.out.println("You have already written a review for user " + receiver + " for the trade with Id " + tradeId);
    }

    public void askRating() {
        System.out.println("If you would like to leave a review of your trading experience," +
            "please enter a rating on a scale of 1-5.");
    }

    public void invalidRating() {
        System.out.println("Invalid rating. Please enter a rating on a scale of 1-5.");
    }

    public void askComment() { System.out.println("If you would like to leave a comment," +
            "please enter it here. If not, press enter."); }
}
