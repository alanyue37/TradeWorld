package undocomponent;

import profilecomponent.ReviewManager;

import java.io.Serializable;

public class UndoAddReview implements UndoableOperation, Serializable {

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
     * Throws NoLongerUndoableException if the review no longer exists.
     */
    @Override
    public void undo() throws NoLongerUndoableException{
        reviewManager.deleteReview(reviewId);
    }

    @Override
    public String toString() {
        return "Review with id " + reviewId + " was added";
    }
}
