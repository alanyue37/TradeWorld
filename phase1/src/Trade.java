import java.util.HashMap;

public abstract class Trade {
    private String type;  //temporary or permanent
    private int numOfEdits;
    private int id;
    private String status; //or maybe boolean?
    private HashMap meeting; //think of something else better
    private boolean confirmed;
    private static int totalTrade; //for id

    /**
     *
     * @param type
     */
    public Trade(String type){
        this.numOfEdits = 0;
        this.type = type;
        this.id = totalTrade; //
    }

}


