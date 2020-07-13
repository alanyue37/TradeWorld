import java.util.List;
import java.util.Map;

public abstract class MenuTextPresenter {

    private Map<Integer, String> menuOptions;

    public MenuTextPresenter() {
    }

    public void addMenuOptions(List<String> options) {
        for (int i=0; i < options.size(); i++) {
            menuOptions.put(i+1, options.get(i));
        }
    }

    public void displayMenu() {
        for (int i=0; i < menuOptions.size(); i++) {
            int num = i + 1;
            System.out.println(num + ". " + menuOptions.get(num));
        }
        System.out.println("Please enter your # choice: ");
    }

}
