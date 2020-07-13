import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProposedTradesController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    ProposedTradesPresenter presenter;
    private String username;

    public ProposedTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ProposedTradesPresenter(tradeModel);
    }

    @Override
    public void run() {
    }
}
