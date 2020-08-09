package undocomponent;

import tradecomponent.TradeManager;

public class UndoAddProposedTrade implements UndoableOperation {
    private final TradeManager tradeManager;
    private final String tradeId;

    /**
     * Instantiates UndoAddProposedTrade
     * @param tradeManager reference to TradeManager reference
     * @param tradeId id of trade proposed
     */
    public UndoAddProposedTrade(TradeManager tradeManager, String tradeId) {
        this.tradeManager = tradeManager;
        this.tradeId = tradeId;
    }

    /**
     * Deletes a proposed trade with tradeId.
     * Throws NoLongerUndoableException if the trade has already progressed to a later stage, such as meeting time has
     * been confirmed or trade has completed.
     */
    @Override
    public void undo() throws NoLongerUndoableException {
        // TODO: add trademanager method call to delete trade with tradeId
    }
}
