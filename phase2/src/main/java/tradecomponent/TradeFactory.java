package tradecomponent;

import java.io.Serializable;
import java.util.List;

/**
 * Manages the creation of trades (one way or two way).
 */
public class TradeFactory implements Serializable {

    /**
     * Returns a new trade that is one way or two way and is permanent or temporary
     *
     * @param way one way or two way trade
     * @param type permanent or temporary
     * @param id the ID of the trade
     * @param details a list of the users and items involved
     * @return the created trade
     */
    public Trade getTrade(String way, String type, String id, List<String> details){
        if (way.equalsIgnoreCase("oneWay")){
            return new OneWayTrade(type, id, details.get(0), details.get(1), details.get(2));
        } else{
            return new TwoWayTrade(type, id, details.get(0), details.get(1), details.get(2), details.get(3));
        }
    }
}

