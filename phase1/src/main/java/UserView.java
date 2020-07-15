/**
 * A class that prints the prompts from UserPresenter. It is the text UI.
 */
public class UserView {         // should this class be deleted since we have an interface already?
    public UserPresenter s;

    public UserView(UserPresenter s) {
        this.s = s;
    }

    /**
     * This method prints the prompts to the screen.
     */
    public void print() {
        System.out.println(s);
    }
}
