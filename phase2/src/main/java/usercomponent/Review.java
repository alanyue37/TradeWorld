package usercomponent;

public class Review {
    private int rating;
    private String comment;
    private String tradeId;
    private String author;
    private String receiver;

    public Review(int rating, String comment, String tradeId, String author, String receiver) {
        this.rating = rating;
        this.comment = comment;
        this.tradeId = tradeId;
        this.author = author;
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }
}

