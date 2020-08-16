package undocomponent;

import tradecomponent.MeetingManager;
import tradecomponent.TradeManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An undoable operation representing a user proposing a trade.
 */
public class UndoAddProposedTrade implements UndoableOperation, Serializable {
    private final TradeManager tradeManager;
    private final MeetingManager meetingManager;
    private final String tradeId;

    /**
     * Instantiates a new UndoAddProposedTrade operation.
     * @param tradeManager reference to TradeManager instance
     * @param meetingManager reference to MeetingManager instance
     * @param tradeId id of trade proposed
     */
    public UndoAddProposedTrade(TradeManager tradeManager, MeetingManager meetingManager, String tradeId) {
        this.tradeManager = tradeManager;
        this.meetingManager = meetingManager;
        this.tradeId = tradeId;
    }

    /**
     * Deletes a proposed trade with tradeId.
     * @throws NoLongerUndoableException if the trade has already progressed to a later stage, such as meeting time has
     * been confirmed or trade has completed
     */
    @Override
    public void undo() throws NoLongerUndoableException {
        List<String> trades = new ArrayList<>();
        trades.add(tradeId);
        if (meetingManager.getToCheckTrades(trades, "proposed").contains(tradeId)) {
            // Confirmed that trade is still at proposed stage
            // Need to check here rather than TradeManager because TradeManager doesn't have access to MeetingManager
            tradeManager.cancelTrade(tradeId);
            meetingManager.cancelMeetingsOfTrade(tradeId); //cancels the meetings associated with this trade too
        }
        else {
            throw new NoLongerUndoableException();
        }
    }

    @Override
    public String toString() {
        return "Trade with id " + tradeId + " was proposed";
    }
}
