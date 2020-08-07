package usercomponent;

import java.io.Serializable;

public class Review implements Serializable {
    private final String id;
    private int rating;
    private String comment;
    private String tradeId;
    private String author;
    private String receiver;
    private String state;

    public Review(String id, int rating, String comment, String tradeId, String author, String receiver) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.tradeId = tradeId;
        this.author = author;
        this.receiver = receiver;
        this.state = "pending";
    }

    /**
     * Returns the id of the review
     * @return  The id of the review
     */
    public String getId() {
        return id;
    }

    public String getTradeId(){
        return tradeId;
    }

    public int getRating() { return rating; }

    public String getComment() { return comment; }

//    public String getAuthor(){
//        return author;
//    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }
}


