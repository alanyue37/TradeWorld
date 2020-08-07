package usercomponent;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ReviewManager implements Serializable {
    private Map<String, List<Review>> userToReviews; // maps username to list of reviews
    private final AtomicInteger counter = new AtomicInteger(); // keeps count of reviews for review id

    public ReviewManager() {
        userToReviews = new HashMap<>();
    }

    public void addReview(int rating, String comment, String tradeId, String author, String receiver) {
        String id = String.valueOf(counter.getAndIncrement());
        Review r = new Review(id, rating, comment, tradeId, author, receiver);
        if (userToReviews.containsKey(receiver)){
            userToReviews.get(receiver).add(r);
        } else{
            List<Review> reviews = new ArrayList<>();
            reviews.add(r);
            userToReviews.put(receiver, reviews);
        }
    }

    public List<String> viewProfile(String username, int num) { // num is how many most recent comments the user will see
        List<String> profileInfo = new ArrayList<>();
        if (!userToReviews.containsKey(username)){
            return profileInfo;
        }
        List<Review> reviews = getAvailableReviews(username);
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

//    public boolean alreadyWroteReview(String writerUsername, String receiverUsername, String tradeId){
//        if (!userToReviews.containsKey(receiverUsername)){
//            return false;
//        } else if(userToReviews.get(receiverUsername).size() == 0){
//            return false;
//        } else{
//            List<Review> reviews = userToReviews.get(receiverUsername);
//            for (Review review: reviews){
//                if (review.getAuthor().equals(writerUsername) && review.getTradeId().equals(tradeId)){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public void verifyReview(String tradeId, List<String> usernames){
        for (String user: usernames){
            for (Review review: userToReviews.get(user)){
                if (review.getTradeId().equals(tradeId)){
                    review.setState("available");
                }
            }
        }
    }

    private List<Review> getAvailableReviews(String username){
        List<Review> availableReviews = new ArrayList<>();
        for (Review review: userToReviews.get(username)){
            if (review.getState().equals("available")){
                availableReviews.add(review);
            }
        }
        return availableReviews;
    }
}
