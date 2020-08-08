package undocomponent;

import usercomponent.ReviewManager;

public class UndoAddReview implements UndoableOperation {

    private final ReviewManager reviewManager;
    private final String reviewId;

    /**
     * Instantiates UndoAddReview
     * @param reviewManager reference to ReviewManager instance
     * @param reviewId id of review added
     */
    public UndoAddReview(ReviewManager reviewManager, String reviewId) {
        this.reviewManager = reviewManager;
        this.reviewId = reviewId;
    }

    /**
     * Deletes review with reviewId
     * Throws NoLongerUndoableException if the item is currently part of an active trade.
     */
    @Override
    public void undo() throws NoLongerUndoableException{
        reviewManager.deleteReview(reviewId);
    }
}
