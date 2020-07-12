import org.junit.*;

import java.util.Date;

import static org.junit.Assert.*;

public class TradeManagerTest {
    static TradeManager tm;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        tm = new TradeManager(3);
    }

    // test that a meeting is added correctly
    @Test(timeout = 50)
    public void testAddMeetingOneWayPermanent() {
        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
        assertTrue("the trade should have one meeting on 1/1/2020 in toronto\n", tm.getTrade(tradeId).getMeetingList().get(0).getLocation().equals("toronto") && tm.getTrade(tradeId).getMeetingList().get(0).getTime().equals(new Date(2020, 1, 1)));
    }

    // test a change to the meeting
    @Test(timeout = 50)
    public void testChangeMeetingOneWayPermanent() {
        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 2, 2));
        assertTrue("the trade should have one meeting on 2/2/2020 in toronto\n", tm.getTrade(tradeId).getMeetingList().get(0).getLocation().equals("toronto") && tm.getTrade(tradeId).getMeetingList().get(0).getTime().equals(new Date(2020, 2, 2)));
    }

    // test agreeing to a meeting
    @Test(timeout = 50)
    public void testAgreeMeetingOneWayPermanent() {
        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 2, 2));
        tm.agreeMeeting(tradeId);
        assertTrue("the meeting should be confirmed\n", tm.getTrade(tradeId).getMeetingList().get(0).getIsConfirmed());

    }

    // test that both users confirm and trade is closed
    @Test(timeout = 50)
    public void testConfirmTradeOneWayPermanent() {
        String tradeId = tm.addOneWayTrade("permanent", "aly", "jiaqi", "book1");
        tm.addMeetingToTrade(tradeId, "toronto", new Date(2020, 1, 1));
        tm.changeMeetingOfTrade(tradeId, "toronto", new Date(2020, 2, 2));
        tm.agreeMeeting(tradeId);
        tm.confirmMeetingHappened(tradeId);
        assertEquals("meeting should have one confirmation\n", 1, tm.getTrade(tradeId).getMeetingList().get(0).getNumConfirmations());
        tm.confirmMeetingHappened(tradeId);
        assertEquals("meeting should have two confirmations\n", 2, tm.getTrade(tradeId).getMeetingList().get(0).getNumConfirmations());
        assertTrue("meeting should be completed\n", tm.getTrade(tradeId).getMeetingList().get(0).getIsCompleted());
        assertFalse("trade should be closed\n", tm.getTrade(tradeId).getIsOpened());
        // NOTE: this does not check if the trade got moved to completedTrades map
    }

}