import java.util.Arrays;
import java.util.List;

public class AdminMainMenuPresenter extends MenuTextPresenter{
    public AdminMainMenuPresenter() {
    }

    public void display() {
        List<String> options = Arrays.asList(new String[] {"Create new admin", "Review items"});
    }
}
