/**
 * A class that prints the prompts from UserPresenter. It is the text UI.
 */
public class UserView {
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
