import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConfirmTradesController implements RunnableController {
    private final BufferedReader br;
    TradeModel tradeModel;
    ConfirmTradesPresenter presenter;
    private String username;

    public ConfirmTradesController(TradeModel tradeModel, String username) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.tradeModel = tradeModel;
        this.username = username;
        presenter = new ConfirmTradesPresenter(tradeModel);
    }

    @Override
    public void run() {}
}

// confirm meeting has happened (for each trade) [Alyssa + JiaQi]
//Add second meeting 30 days later. This should show up in confirm trades once date has passed.
//One-way (temp and permanent): update user credit