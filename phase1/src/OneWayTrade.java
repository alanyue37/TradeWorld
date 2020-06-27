public class OneWayTrade extends Trade {
    private int itemId;
    private String giverUsername;
    private String receiverUsername;


    /**
     * @param type
     */
    public OneWayTrade(String type) {
        super(type);
    }
}
