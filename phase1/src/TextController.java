import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TextController {

    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final String username;
    private final TextPresenter presenter;

    public TextController(TradeModel tradeModel, String username) {
        this.tradeModel = tradeModel;
        this.username = username;
        br = new BufferedReader(new InputStreamReader(System.in));
        presenter = new TextPresenter();
    }

    private List<String> getItemsInfo(List<String> itemIds) {
        List <String> itemsInfo = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsInfo.add(tradeModel.getItemManager().getItemInfo(itemId));
        }
        return itemsInfo;
    }
}