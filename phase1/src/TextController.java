import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class TextController implements RunnableController {
    private final BufferedReader br;
    private final TradeModel tradeModel;
    private final UserPresenter presenter;
    private final String username;

    public TextController(TradeModel tradeModel, String username) {
        this.tradeModel = tradeModel;
        this.username = username;
        this.br = new BufferedReader(new InputStreamReader(System.in));
        presenter = null;
    }
}
