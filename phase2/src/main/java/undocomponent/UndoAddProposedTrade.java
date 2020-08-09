package undocomponent;

import tradecomponent.MeetingManager;
import tradecomponent.TradeManager;

import java.util.ArrayList;
import java.util.List;

public class UndoAddProposedTrade implements UndoableOperation {
    private final TradeManager tradeManager;
    private final MeetingManager meetingManager;
    private final String tradeId;

    /**
     * Instantiates UndoAddProposedTrade
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
     * Throws NoLongerUndoableException if the trade has already progressed to a later stage, such as meeting time has
     * been confirmed or trade has completed.
     */
    @Override
    public void undo() throws NoLongerUndoableException {
        List<String> trades = new ArrayList<>();
        trades.add(tradeId);
        if (meetingManager.getToCheckTrades(trades, "proposed").contains(tradeId)) {
            // Confirmed that trade is still at proposed stage
            // Need to check here rather than TradeManager because TradeManager doesn't have access to MeetingManager
            tradeManager.deleteProposedTrade(tradeId);
        }
        else {
            throw new NoLongerUndoableException();
        }

    }
}
