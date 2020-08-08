package tradecomponent;

import java.util.List;

public class TradeFactory {

    public Trade getTrade(String way, String type, String id, List<String> details){
        if (way.equalsIgnoreCase("oneWay")){
            return new OneWayTrade(type, id, details.get(0), details.get(1), details.get(2));
        } else{
            return new TwoWayTrade(type, id, details.get(0), details.get(1), details.get(2), details.get(3));
        }
    }
}

