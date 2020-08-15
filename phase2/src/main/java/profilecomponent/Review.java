package profilecomponent;

import java.io.Serializable;

/**
 * Represents a review for a trade
 */
public class Review implements Serializable {
    private final String id;
    private int rating;
    private String comment;
    private String tradeId;
    private String author;
    private String receiver;

    /**
     * Initiate a new review.
     * @param id ID of the review
     * @param rating numerical rating (1-5) of the trade experience
     * @param comment comment on the trade experience
     * @param tradeId trade id of the trade this review is related to
     * @param author review writer
     * @param receiver review receiver
     */
    public Review(String id, int rating, String comment, String tradeId, String author, String receiver) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.tradeId = tradeId;
        this.author = author;
        this.receiver = receiver;
    }

    /**
     * Returns the ID of the review
     * @return  The ID of the review
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the ID of the trade that this review is related to.
     * @return ID of trade
     */
    public String getTradeId(){
        return tradeId;
    }

    /**
     * Returns the rating (score 1 to 5) of the review.
     * @return rating score of review
     */
    public int getRating() { return rating; }

    /**
     * Return the comment of the review.
     * @return comment
     */
    public String getComment() { return comment; }

    /**
     * Return the author of the review.
     * @return author
     */
    public String getAuthor(){
        return author;
    }
}


