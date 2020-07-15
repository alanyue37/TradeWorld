//import org.junit.*;
//
//import java.util.Date;
//
//import static org.junit.Assert.*;
//
//public class TradeManagerTest {
//    static TradeManager tm;
//
//    @BeforeClass
//    public static void setUpBeforeClass() throws Exception {
//        tm = new TradeManager(3);
//    }
//
//    // test that a meeting is added correctly
//    @Test(timeout = 50)
//    public void testAddMeetingOneWayPermanent() {
//        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
//        assertTrue("the trade should have one meeting on 1/1/2020 in toronto\n", tm.getTrade(tradeId).getMeetingList().get(0).getLocation().equals("toronto") && tm.getTrade(tradeId).getMeetingList().get(0).getTime().equals(new Date(2020, 1, 1)));
//        // NOTE: this does not check if the trade got added to the ongoingTrades map
//    }
//
//    // test a change to the meeting
//    @Test(timeout = 50)
//    public void testChangeMeetingOneWayPermanent() {
//        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 2, 2));
//        assertTrue("the trade should have one meeting on 2/2/2020 in toronto\n", tm.getTrade(tradeId).getMeetingList().get(0).getLocation().equals("toronto") && tm.getTrade(tradeId).getMeetingList().get(0).getTime().equals(new Date(2020, 2, 2)));
//        assertEquals("meeting should have one edit\n", 1, tm.getTrade(tradeId).getMeetingList().get(0).getNumOfEdits());
//    }
//
//    // test that a trade needs to be cancelled
//    @Test(timeout = 50)
//    public void testCancelOneWayPermanent() {
//        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 2, 2));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 3, 3));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 4, 4));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 5, 5));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 6, 6));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 7, 7));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 8, 8));
//        assertEquals("meeting should have seven edits\n", 7, tm.getTrade(tradeId).getMeetingList().get(0).getNumOfEdits());
//        assertTrue("the trade needs to be cancelled\n", tm.needCancelTrade(tradeId));
//    }
//
//    // test agreeing to a meeting
//    @Test(timeout = 50)
//    public void testAgreeMeetingOneWayPermanent() {
//        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 2, 2));
//        tm.agreeMeeting(tradeId);
//        assertTrue("the meeting should be confirmed\n", tm.getTrade(tradeId).getMeetingList().get(0).getIsConfirmed());
//
//    }
//
//    // test that both users confirm and trade is closed
//    @Test(timeout = 50)
//    public void testConfirmTradeOneWayPermanent() {
//        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 2, 2));
//        tm.agreeMeeting(tradeId);
//        tm.confirmMeetingHappened(tradeId);
//        assertEquals("meeting should have one confirmation\n", 1, tm.getTrade(tradeId).getMeetingList().get(0).getNumConfirmations());
//        tm.confirmMeetingHappened(tradeId);
//        assertEquals("meeting should have two confirmations\n", 2, tm.getTrade(tradeId).getMeetingList().get(0).getNumConfirmations());
//        assertTrue("meeting should be completed\n", tm.getTrade(tradeId).getMeetingList().get(0).getIsCompleted());
//        assertFalse("trade should be closed\n", tm.getTrade(tradeId).getIsOpened());
//        // NOTE: this does not check if the trade got moved to completedTrades map
//    }
//
//    // test that a meeting is added correctly
//    @Test(timeout = 50)
//    public void testAddMeetingOneWayTemporary() {
//        String tradeId = tm.addOneWayTrade("temporary", "jiaqi", "aly", "book2");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 3, 3));
//        assertTrue("the trade should have one meeting on 3/3/2020 in toronto\n", tm.getTrade(tradeId).getMeetingList().get(0).getLocation().equals("toronto") && tm.getTrade(tradeId).getMeetingList().get(0).getTime().equals(new Date(2020, 3, 3)));
//        // NOTE: this does not check if the trade got added to the ongoingTrades map
//    }
//
//    // test several changes to the meeting
//    @Test(timeout = 50)
//    public void testChangeMeetingOneWayTemporary() {
//        String tradeId = tm.addOneWayTrade("temporary", "jiaqi", "aly", "book2");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 3, 3));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 4, 4));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 5, 5));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 6, 6));
//        assertTrue("the trade should have one meeting on 6/6/2020 in toronto\n", tm.getTrade(tradeId).getMeetingList().get(0).getLocation().equals("toronto") && tm.getTrade(tradeId).getMeetingList().get(0).getTime().equals(new Date(2020, 6, 6)));
//        assertEquals("meeting should have three edits\n", 3, tm.getTrade(tradeId).getMeetingList().get(0).getNumOfEdits());
//    }
//
//    // test that a trade needs to be cancelled
//    @Test(timeout = 50)
//    public void testCancelOneWayTemporary() {
//        String tradeId = tm.addOneWayTrade("temporary", "jiaqi", "aly", "book2");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 3, 3));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 4, 4));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 5, 5));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 6, 6));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 7, 7));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 8, 8));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 9, 9));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 10, 10));
//        assertEquals("meeting should have seven edits\n", 7, tm.getTrade(tradeId).getMeetingList().get(0).getNumOfEdits());
//        assertTrue("the trade needs to be cancelled\n", tm.needCancelTrade(tradeId));
//    }
//
//    // test agreeing to a meeting
//    @Test(timeout = 50)
//    public void testAgreeMeetingOneWayTemporary() {
//        String tradeId = tm.addOneWayTrade("temporary", "jiaqi", "aly", "book2");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 3, 3));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 4, 4));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 5, 5));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 6, 6));
//        tm.agreeMeeting(tradeId);
//        assertTrue("the meeting should be confirmed\n", tm.getTrade(tradeId).getMeetingList().get(0).getIsConfirmed());
//
//    }
//
//    // test that both users confirm
//    @Test(timeout = 50)
//    public void testConfirmTradeOneWayTemporary() {
//        String tradeId = tm.addOneWayTrade("temporary", "jiaqi", "aly", "book2");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 3, 3));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 4, 4));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 5, 5));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 6, 6));
//        tm.agreeMeeting(tradeId);
//        tm.confirmMeetingHappened(tradeId);
//        assertEquals("meeting should have one confirmation\n", 1, tm.getTrade(tradeId).getMeetingList().get(0).getNumConfirmations());
//        tm.confirmMeetingHappened(tradeId);
//        assertEquals("meeting should have two confirmations\n", 2, tm.getTrade(tradeId).getMeetingList().get(0).getNumConfirmations());
//        assertTrue("meeting should be completed\n", tm.getTrade(tradeId).getMeetingList().get(0).getIsCompleted());
//        assertTrue("trade should be open\n", tm.getTrade(tradeId).getIsOpened());
//    }
//
//    // test that both users agree to second meeting, confirm, and the trade is closed
//    @Test(timeout = 50)
//    public void testCloseTradeOneWayTemporary() {
//        String tradeId = tm.addOneWayTrade("temporary", "jiaqi", "aly", "book2");
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 3, 3));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 4, 4));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 5, 5));
//        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 6, 6));
//        tm.agreeMeeting(tradeId);
//        tm.confirmMeetingHappened(tradeId);
//        tm.confirmMeetingHappened(tradeId);
//        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 7, 6));
//        assertTrue("the trade should have a second meeting on 6/7/2020 (30 days later) in toronto\n", tm.getTrade(tradeId).getMeetingList().get(1).getLocation().equals("toronto") && tm.getTrade(tradeId).getMeetingList().get(1).getTime().equals(new Date(2020, 7, 6)));
//        tm.agreeMeeting(tradeId);
//        tm.confirmMeetingHappened(tradeId);
//        assertEquals("meeting should have one confirmation\n", 1, tm.getTrade(tradeId).getMeetingList().get(1).getNumConfirmations());
//        tm.confirmMeetingHappened(tradeId);
//        assertEquals("meeting should have two confirmations\n", 2, tm.getTrade(tradeId).getMeetingList().get(1).getNumConfirmations());
//        assertTrue("meeting should be completed\n", tm.getTrade(tradeId).getMeetingList().get(1).getIsCompleted());
//        assertFalse("trade should be closed\n", tm.getTrade(tradeId).getIsOpened());
//        // NOTE: this does not check if the trade got moved to completedTrades map
//    }
//}