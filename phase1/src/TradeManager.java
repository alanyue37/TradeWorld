import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class TradeManager implements Serializable {
    private int limitOfEdits;
    private ArrayList<Trade> listOfTrades;
    private MeetingManager meetingManager;

    public void addTrade(Trade newTrade){
        this.listOfTrades.add(newTrade);
    }

    public ArrayList<Trade> getAllTrades(){
        return this.listOfTrades;
    }

    public int getLimitEdits(){
        return this.limitOfEdits;
    }

    public void changeLimitEdits(int newLimit){
        this.limitOfEdits = newLimit;
    }

    public ArrayList<Trade> getOpenTrades(){
        ArrayList<Trade> openTrades = new ArrayList<Trade>();
        for (int i=0; i < this.listOfTrades.size(); i++){
            if (listOfTrades.get(i).getIsOpened()){
                openTrades.add(listOfTrades.get(i));
            }
        }
        return openTrades;
    }

    public boolean isIncompleteTrade(Trade openTrade) {
        if (openTrade.getTradeType().equals("permanent")) {
            if (meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(0))) {
                return true;
            }
        }
        if (openTrade.getTradeType().equals("temporary")){
            if (meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(0))){
                return true;
            }
            if (openTrade.getMeetingList().get(0).getIsCompleted()){
                return meetingManager.isIncompleteMeeting(openTrade.getMeetingList().get(1));
            }
        }
        return false;
    }

    public ArrayList<Trade> getIncompleteTrade(){
        ArrayList<Trade> openTrades = getOpenTrades();
        ArrayList<Trade> incompleteTrades = new ArrayList<Trade>();
        for (Trade openTrade : openTrades) {
            if (isIncompleteTrade(openTrade)) {
                incompleteTrades.add(openTrade);
            }
        }
        return incompleteTrades;
    }

    // limit of edits?

}