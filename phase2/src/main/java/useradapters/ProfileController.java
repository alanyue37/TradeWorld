package useradapters;

import tradegateway.TradeModel;
import trademisc.RunnableController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProfileController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final ProfilePresenter presenter;
    private final String username;

    public ProfileController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ProfilePresenter();
    }

    @Override
    public void run() {
        try {
            browseProfile();
        } catch (IOException e) {
            System.out.println("Something bad happened.");
        }
    }

    private boolean browseProfile() throws IOException {
        presenter.showOptions();
        String input = br.readLine();
        switch (input) {
            case "1": // set/unset vacation
                editVacation();
                break;
            case "2": // private/public account
                accountPrivacy();
                break;
            case "3": // manage friend requests
                reviewFriendRequests();
                break;
            case "4": // adding friend (send friend request)
                addFriend();
                break;
            case "5": // view friend list
                viewFriends();
            case "6": // view account setting: privacy, vacation, city
                viewAccountSetting();
            case "exit":
                return false;
            default:
                presenter.tryAgain();
        }
        return true;
    }


    private void editVacation() throws IOException {
        presenter.accountModeSelection();
        String confirmationInput = br.readLine();
        while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
            presenter.invalidInput();
            confirmationInput = br.readLine();
        }
        tradeModel.getUserManager().setOnVacation(username, confirmationInput.equals("1"));
    }

    private void accountPrivacy() throws IOException {
        presenter.accountPrivacySelection();
        String confirmationInput = br.readLine();
        while (!confirmationInput.equals("0") && !confirmationInput.equals("1")) {
            presenter.invalidInput();
            confirmationInput = br.readLine();
        }
        tradeModel.getUserManager().setPrivate(username, confirmationInput.equals("1"));
    }

    private void reviewFriendRequests() throws IOException {
        Set<String> requests = tradeModel.getUserManager().getFriendRequests(username);
        for (String user : requests) {
            presenter.manageRequest(user);
            String confirmationInput = br.readLine();
            while (!confirmationInput.equals("0") && !confirmationInput.equals("1") && !confirmationInput.equals("2")) {
                presenter.invalidInput();
                confirmationInput = br.readLine();
            }
            if (!confirmationInput.equals("0")){
                tradeModel.getUserManager().setFriendRequest(username, user, confirmationInput.equals("1"));
            }
        }
    }

    private void addFriend() throws IOException {
        presenter.printEnterFriendUsername();
        String friend = br.readLine();
        if (!tradeModel.getUserManager().containsTradingUser(friend)){
            presenter.printSearchingInvalid();
        } else if(tradeModel.getUserManager().getFriendList(username).contains(friend)){
            presenter.alreadyFriend();
        } else if(tradeModel.getUserManager().getFriendRequests(friend).contains(username)){
            presenter.alreadySentRequest();
        }
        else {
            tradeModel.getUserManager().sendFriendRequest(friend, username);
            presenter.addedFriend();
        }
    }

    private void viewAccountSetting(){
        String privacy;
        String vacation;
        if (tradeModel.getUserManager().getPrivateUser().contains(username)){
            privacy = "private";
        } else{ privacy = "public";}
        if (tradeModel.getUserManager().getOnVacation().contains(username)){
            vacation = "on";
        } else{ vacation = "off";}
        String city = tradeModel.getUserManager().getCityByUsername(username);
        presenter.printViewAccountSettings(privacy, vacation, city);
    }

    private void viewFriends(){
        List<String> friends = new ArrayList<>(tradeModel.getUserManager().getFriendList(username));
        presenter.printViewFriends(friends);
    }

}
