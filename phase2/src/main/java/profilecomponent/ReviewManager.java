package profilecomponent;

import com.google.gson.Gson;
import tradegateway.ObservableDataModel;
import undocomponent.NoLongerUndoableException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the creation and handling of reviews for trades.
 */
public class ReviewManager implements Serializable {
    private final ObservableDataModel observableDataModel;
    private final Map<String, List<Review>> userToReviews; // maps username to list of reviews
    private final AtomicInteger counter = new AtomicInteger(); // keeps count of reviews for review ID

    /**
     * Instantiates a ReviewManager.
     */
    public ReviewManager(ObservableDataModel observableDataModel) {
        userToReviews = new HashMap<>();
        this.observableDataModel = observableDataModel;
    }

    /**
     * Creates a new review and adds it to the receiver's list of reviews.
     * @param rating rating of review
     * @param comment comment of review
     * @param tradeId trade ID of review
     * @param author author of review
     * @param receiver receiver of review
     * @return ID of the review
     */
    public String addReview(int rating, String comment, String tradeId, String author, String receiver) {
        String id = String.valueOf(counter.getAndIncrement());
        Review r = new Review(id, rating, comment, tradeId, author, receiver);
        if (userToReviews.containsKey(receiver)){
            userToReviews.get(receiver).add(r);
        } else{
            List<Review> reviews = new ArrayList<>();
            reviews.add(r);
            userToReviews.put(receiver, reviews);
        }
        observableDataModel.setChanged();
        return r.getId();
    }

    /**
     * Returns the review profile of a given user.
     * @param username username of whom we want to get their review profile
     * @param num num of reviews from this user we want to get
     * @return review profile of a user
     */
    public List<String> viewProfile(String username, int num) { // num is how many most recent comments the user will see
        List<String> profileInfo = new ArrayList<>();
        if (!userToReviews.containsKey(username)){
            return profileInfo;
        }
        List<Review> reviews = userToReviews.get(username);
        if (reviews.size() == 0) {
            return profileInfo;
        }
        int totalRatings = 0;
        for (Review review : reviews) {
            totalRatings += review.getRating();
        }
        int averageRating = totalRatings / reviews.size();
        profileInfo.add(String.valueOf(averageRating));
        int i = reviews.size() - 1;
        while (i >= reviews.size() - num && i >= 0) {
            profileInfo.add(reviews.get(i).getComment());
            i--;
        }
        return profileInfo;
    }

    /**
     * Returns whether the user already wrote a review to their trader for a given trade.
     * @param writerUsername username of the author of the review
     * @param receiverUsername username of receiver of review
     * @param tradeId trade id that the review is related to
     * @return whether the user already wrote a review for a particular trade and user.
     */
    public boolean alreadyWroteReview(String writerUsername, String receiverUsername, String tradeId){
        if (!userToReviews.containsKey(receiverUsername)){
            return false;
        } else if(userToReviews.get(receiverUsername).size() == 0){
            return false;
        } else{
            List<Review> reviews = userToReviews.get(receiverUsername);
            for (Review review: reviews){
                if (review.getAuthor().equals(writerUsername) && review.getTradeId().equals(tradeId)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Delete review with given reviewId.
     * @param reviewId id of review to be deleted
     * @throws NoLongerUndoableException if no review with reviewId exists
     */
    public void deleteReview(String reviewId) throws NoLongerUndoableException {
        String username = null;
        Review reviewToBeDeleted = null;
        for (String u: userToReviews.keySet()) {
            List<Review> reviewsList = userToReviews.get(u);
            for (Review review :reviewsList) {
                if (review.getId().equals(reviewId)) {
                    username = u;
                    reviewToBeDeleted = review;
                }
            }
        }
        if (reviewToBeDeleted == null) {
            throw new NoLongerUndoableException();
        }
        userToReviews.get(username).remove(reviewToBeDeleted);
        observableDataModel.setChanged();
    }

    /**
     * Gets a String representation of the reviews for a user
     * @param receiverUsername username of the user to get their reviews
     * @return String of the reviews for user with username "receiverUsername"
     */
    public String getReviewsByUser(String receiverUsername) {
        Gson gson = new Gson();
        List<Review> reviews = userToReviews.get(receiverUsername);
        List<Map<String, String>> reviewMaps = new ArrayList<>();
        Map<String, String> averageRatingMap = new HashMap<>();
        reviewMaps.add(0, averageRatingMap);

        if (reviews == null) {
            averageRatingMap.put("average", "N/A");
            return gson.toJson(reviewMaps);
        }

        float totalRating = 0;
        for (Review r: reviews) {
            Map<String, String> reviewMap = new HashMap<>();
            reviewMap.put("id", r.getId());
            reviewMap.put("comment", r.getComment());
            reviewMap.put("rating", String.valueOf(r.getRating()));
            totalRating += r.getRating();
            reviewMap.put("author", r.getAuthor());
            reviewMap.put("tradeId", r.getTradeId());
            reviewMaps.add(reviewMap);
        }

        float averageRating = totalRating / reviews.size();
        averageRatingMap.put("average", String.format("%.2f", averageRating));

        return gson.toJson(reviewMaps);
    }
}