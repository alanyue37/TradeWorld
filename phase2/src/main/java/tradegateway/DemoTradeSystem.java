package tradegateway;

import adminadapters.AdminMainGUI;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import loginadapters.LoginGUI;
import tradeadapters.ConfirmTradesController;
import tradeadapters.InitiateTradeController;
import tradeadapters.ProposedTradesController;
import trademisc.RunnableGUI;

import java.io.IOException;
import java.util.*;

/**
 * DEMO trading system with pre-inserted users, items, etc. solely for internal testing
 * INTENTIONALLY DOES NOT TEST PERSISTENCE
 */
public class DemoTradeSystem {

    private final String tradeModelFile = "serializedobjects.ser";
    private final String logoFile = "logo.png";
    private DataManager dataManager;
    private TradeModel tradeModel;
    private RunnableGUI gui;

    /**
     * Run the trading system.
     */
    public void run(Stage stage) {
        try {
            dataManager = new DataManager(tradeModelFile);
            tradeModel = dataManager.readFromFile();

            // INITIALIZE TRADEMODEL FOR DEMO
            initializeTradeModel(tradeModel);
            Image logo = dataManager.readImage(logoFile);

            gui = new LoginGUI(stage, 500, 650, tradeModel, logo);
            //gui = new ProfileGUI(stage, 720, 600, tradeModel, false);
            //gui = new LoggedInProfileGUI(stage, 720, 600, tradeModel, false);
            //gui = new AdminMainGUI(800, 800, tradeModel);
            //gui = new UserMainGUI(800, 800, tradeModel, "u1");
            gui.showScreen();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void persist() {
        try {
            tradeModel.clearObservers();
            dataManager.saveToFile(tradeModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTradeModel(TradeModel tradeModel) {
        // IF USERS HAVE ALREADY BEEN ADDED THEN SKIP INITIALIZATION (i.e. reading from persisted data)
        if (tradeModel.getUserManager().login("a1", "a1")) {
            return;
        }

        // DEMO  --- START
        tradeModel.getUserManager().createAdminUser("A_1", "a1", "a1");
        tradeModel.getUserManager().createAdminUser("A_2", "a2", "a2");
        tradeModel.getUserManager().createAdminUser("A_3", "a3", "a3");
        tradeModel.getUserManager().createTradingUser("U_1", "u1", "u1", "Toronto");
        tradeModel.getUserManager().createTradingUser("U_2", "u2", "u2", "Toronto");
        tradeModel.getUserManager().createTradingUser("U_3", "u3", "u3", "Edmonton");
        tradeModel.getUserManager().createTradingUser("U_4", "u4", "u4", "Edmonton");
        tradeModel.getUserManager().createTradingUser("U_5", "u5", "u5", "Mississauga");

        String[] users = new String[] {"u1", "u2", "u3", "u4"};
        for (String username: users) {
            for (int i = 0; i < 5; ++i) {
                tradeModel.getUserManager().updateCreditByUsername(username, true);
            }
        }

        // u1 items
        String u1_1 = tradeModel.getItemManager().addItem("red apple", "u1", "One red apple");
        String u1_2 = tradeModel.getItemManager().addItem("green apple", "u1", "One green apple");
        String u1_3 = tradeModel.getItemManager().addItem("yellow apple", "u1", "One yellow apple");

        // u2 items
        String u2_1 = tradeModel.getItemManager().addItem("red car", "u2", "One red car");
        String u2_2 = tradeModel.getItemManager().addItem("green car", "u2", "One green car");
        String u2_3 = tradeModel.getItemManager().addItem("yellow car", "u2", "One yellow car");

        // u3 items
        String u3_1 = tradeModel.getItemManager().addItem("red book", "u3", "One red book");
        String u3_2 = tradeModel.getItemManager().addItem("green book", "u3", "One green book");
        String u3_3 = tradeModel.getItemManager().addItem("yellow book", "u3", "One yellow book");

        // u4 items
        String u4_1 = tradeModel.getItemManager().addItem("red ball", "u4", "One red ball");
        String u4_2 = tradeModel.getItemManager().addItem("green ball", "u4", "One green ball");
        String u4_3 = tradeModel.getItemManager().addItem("yellow ball", "u4", "One yellow ball");

        // Confirm all of u1 items (apples)
        // Confirm all of u2 items (cars)
        // Confirm ONLY first two of u3 items (red and green books)
        // Confirm ONLY first two of u4 items (red and green balls)
        String[] items = new String[] {u1_1, u1_2, u1_3, u2_1, u2_2, u2_3, u3_1, u3_2, u4_1, u4_2};
        for (String itemId : items) {
            tradeModel.getItemManager().confirmItem(itemId);
        }

//        // completed trades
//        // two-way permanent trade with u2 and u1
//        String complete1 = tradeModel.getTradeManager().addTrade("twoWay", "permanent", new ArrayList<>(Arrays.asList("u2", "u1", "3", "2")));
//        String meeting1 = tradeModel.getMeetingManager().createMeeting("toronto", new Date(2020, Calendar.JANUARY, 20, 3, 0), "u2", complete1);
//        tradeModel.getTradeManager().addMeetingToTrade(complete1, meeting1);
//        tradeModel.getMeetingManager().confirmAgreement(meeting1);
//        tradeModel.getMeetingManager().meetingHappened(meeting1, "u1");
//        tradeModel.getMeetingManager().meetingHappened(meeting1, "u2");
//        tradeModel.getTradeManager().closeTrade(complete1);
//        Map<String, List<String>> itemToUsers1 = tradeModel.getTradeManager().itemToUsers(complete1);
//        for (String item : itemToUsers1.keySet()) {
//            tradeModel.getUserManager().removeFromWishlist(itemToUsers1.get(item).get(1), item);
//            tradeModel.getItemManager().setOwner(item, itemToUsers1.get(item).get(1));
//            tradeModel.getItemManager().setItemAvailable(item, true);
//            tradeModel.getUserManager().updateCreditByUsername(itemToUsers1.get(item).get(0), true);
//            tradeModel.getUserManager().updateCreditByUsername(itemToUsers1.get(item).get(1), false);
//        }
//
//        // one-way permanent trade with u2 and u1
//        String complete2 = tradeModel.getTradeManager().addTrade("oneWay", "permanent", new ArrayList<>(Arrays.asList("u1", "u2", "0")));
//        String meeting2 = tradeModel.getMeetingManager().createMeeting("toronto", new Date(2020, Calendar.MARCH, 21, 5, 0), "u2", complete2);
//        tradeModel.getTradeManager().addMeetingToTrade(complete2, meeting2);
//        tradeModel.getMeetingManager().confirmAgreement(meeting2);
//        tradeModel.getMeetingManager().meetingHappened(meeting2, "u1");
//        tradeModel.getMeetingManager().meetingHappened(meeting2, "u2");
//        tradeModel.getTradeManager().closeTrade(complete2);
//        Map<String, List<String>> itemToUsers2 = tradeModel.getTradeManager().itemToUsers(complete2);
//        for (String item : itemToUsers2.keySet()) {
//            tradeModel.getUserManager().removeFromWishlist(itemToUsers2.get(item).get(1), item);
//            tradeModel.getItemManager().setOwner(item, itemToUsers2.get(item).get(1));
//            tradeModel.getItemManager().setItemAvailable(item, true);
//            tradeModel.getUserManager().updateCreditByUsername(itemToUsers2.get(item).get(0), true);
//            tradeModel.getUserManager().updateCreditByUsername(itemToUsers2.get(item).get(1), false);
//        }
//
//        // two-way temporary trade with u4 and u3
//        String complete3 = tradeModel.getTradeManager().addTrade("twoWay", "temporary", new ArrayList<>(Arrays.asList("u4", "u3", "8", "7")));
//        String meeting31 = tradeModel.getMeetingManager().createMeeting("edmonton", new Date(2020, Calendar.JANUARY, 12, 10, 30), "u4", complete3);
//        tradeModel.getTradeManager().addMeetingToTrade(complete3, meeting31);
//        tradeModel.getMeetingManager().confirmAgreement(meeting31);
//        tradeModel.getMeetingManager().meetingHappened(meeting31, "u4");
//        tradeModel.getMeetingManager().meetingHappened(meeting31, "u3");
//        // second meeting
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(tradeModel.getMeetingManager().getLastMeetingTime(complete3));
//        cal.add(Calendar.DATE, 30);
//        Date newDate = cal.getTime();
//        String meeting32 = tradeModel.getMeetingManager().createMeeting(tradeModel.getMeetingManager().getLastMeetingLocation(complete3), newDate, "u4", complete3);
//        tradeModel.getTradeManager().addMeetingToTrade(complete3, meeting32);
//        tradeModel.getMeetingManager().confirmAgreement(meeting32);
//        tradeModel.getMeetingManager().meetingHappened(meeting32, "u4");
//        tradeModel.getMeetingManager().meetingHappened(meeting32, "u3");
//        tradeModel.getTradeManager().closeTrade(complete3);
//        Map<String, List<String>> itemToUsers3 = tradeModel.getTradeManager().itemToUsers(complete3);
//        for (String item : itemToUsers3.keySet()) {
//            tradeModel.getUserManager().removeFromWishlist(itemToUsers3.get(item).get(1), item);
//            tradeModel.getItemManager().setOwner(item, itemToUsers3.get(item).get(1));
//            tradeModel.getItemManager().setItemAvailable(item, true);
//            tradeModel.getUserManager().updateCreditByUsername(itemToUsers3.get(item).get(0), true);
//            tradeModel.getUserManager().updateCreditByUsername(itemToUsers3.get(item).get(1), false);
//        }
//
//        // one-way temporary trade with u4 and u3
//        String complete4 = tradeModel.getTradeManager().addTrade("oneWay", "temporary", new ArrayList<>(Arrays.asList("u3", "u4", "6")));
//        String meeting41 = tradeModel.getMeetingManager().createMeeting("edmonton", new Date(2019, Calendar.SEPTEMBER, 25, 12, 0), "u4", complete4);
//        tradeModel.getTradeManager().addMeetingToTrade(complete4, meeting41);
//        tradeModel.getMeetingManager().confirmAgreement(meeting41);
//        tradeModel.getMeetingManager().meetingHappened(meeting41, "u4");
//        tradeModel.getMeetingManager().meetingHappened(meeting41, "u3");
//        // second meeting
//        cal.setTime(tradeModel.getMeetingManager().getLastMeetingTime(complete4));
//        cal.add(Calendar.DATE, 30);
//        newDate = cal.getTime();
//        String meeting42 = tradeModel.getMeetingManager().createMeeting(tradeModel.getMeetingManager().getLastMeetingLocation(complete4), newDate, "u4", complete4);
//        tradeModel.getTradeManager().addMeetingToTrade(complete4, meeting42);
//        tradeModel.getMeetingManager().confirmAgreement(meeting42);
//        tradeModel.getMeetingManager().meetingHappened(meeting42, "u3");
//        tradeModel.getMeetingManager().meetingHappened(meeting42, "u4");
//        tradeModel.getTradeManager().closeTrade(complete4);
//        Map<String, List<String>> itemToUsers4 = tradeModel.getTradeManager().itemToUsers(complete4);
//        for (String item : itemToUsers4.keySet()) {
//            tradeModel.getUserManager().removeFromWishlist(itemToUsers4.get(item).get(1), item);
//            tradeModel.getItemManager().setOwner(item, itemToUsers4.get(item).get(1));
//            tradeModel.getItemManager().setItemAvailable(item, true);
//            tradeModel.getUserManager().updateCreditByUsername(itemToUsers4.get(item).get(0), true);
//            tradeModel.getUserManager().updateCreditByUsername(itemToUsers4.get(item).get(1), false);
//        }
//
//        // ongoing trades
//        // two-way permanent trade with u2 and u1
//        String ongoing1 = tradeModel.getTradeManager().addTrade("twoWay", "permanent", new ArrayList<>(Arrays.asList("u2", "u1", "4", "1")));
//        String meeting5 = tradeModel.getMeetingManager().createMeeting("toronto", new Date(2020, Calendar.AUGUST, 1, 5, 45), "u2", ongoing1);
//        tradeModel.getTradeManager().addMeetingToTrade(ongoing1, meeting5);
//
//        // one-way temporary trade with u3 and u4
//        String ongoing2 = tradeModel.getTradeManager().addTrade("oneWay", "temporary", new ArrayList<>(Arrays.asList("u4", "u3", "9")));
//        String meeting61 = tradeModel.getMeetingManager().createMeeting("edmonton", new Date(2020, Calendar.MAY, 20, 20, 6), "u3", ongoing2);
//        tradeModel.getTradeManager().addMeetingToTrade(ongoing2, meeting61);

        // DEMO  --- END
    }
}
