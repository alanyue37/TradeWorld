package usercomponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewManager {
    private Map<String, List<Review>> userToReviews; // maps username to list of reviews
    private Map<String, List<Review>> pendingReviews; // maps tradeId to list of reviews

    public ReviewManager() {
        userToReviews = new HashMap<>();
        pendingReviews = new HashMap<>();
    }

    public void addReview(int rating, String comment, String tradeId, String author, String receiver) {
        Review r = new Review(rating, comment, tradeId, author, receiver);
        if (pendingReviews.containsKey(tradeId)) {
            pendingReviews.get(tradeId).add(r);
        } else {
            List<Review> reviews = new ArrayList<>();
            reviews.add(r);
            pendingReviews.put(tradeId, reviews);
        }
    }

    public void verifyReview(String tradeId) { // when trade is completed
        if (pendingReviews.get(tradeId) != null) { // users could have the option to not leave a review, then there might not be any reviews for a trade
            for (Review review : pendingReviews.get(tradeId)) {
                if (userToReviews.containsKey(review.getReceiver())) {
                    userToReviews.get(review.getReceiver()).add(review);
                } else {
                    List<Review> reviews = new ArrayList<>();
                    reviews.add(review);
                    userToReviews.put(review.getReceiver(), reviews);
                }
            }
            pendingReviews.remove(tradeId);
        }
    }

    public List<String> viewProfile(String username, int num) { // num is how many most recent comments the user will see
        List<Review> reviews = userToReviews.get(username);
        List<String> profileInfo = new ArrayList<>();
        if (reviews == null) {
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
}
