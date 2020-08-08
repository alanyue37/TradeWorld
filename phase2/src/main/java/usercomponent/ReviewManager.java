package usercomponent;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ReviewManager implements Serializable {
    private Map<String, List<Review>> userToReviews; // maps username to list of reviews
    private final AtomicInteger counter = new AtomicInteger(); // keeps count of reviews for review id

    /**
     * Instantiates a ReviewManager.
     */
    public ReviewManager() {
        userToReviews = new HashMap<>();
    }

    /**
     * Creates a new review and adds it to the receiver's list of reviews.
     * @param rating rating of review
     * @param comment comment of review
     * @param tradeId trade id of review
     * @param author author of review
     * @param receiver receiver of review
     * @return
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

    public boolean deleteReview(String reviewId) {
        // TODO: implement once pendingReviews and UserToReviews is merged.
        return true;
    }
}
