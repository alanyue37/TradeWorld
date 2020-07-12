/**
 * A class that prints the prompts from AdminPresenter. It is the text UI.
 */
public class AdminView {
    AdminPresenter s;

    public AdminView(AdminPresenter s) {
       this.s = s;
    }

    /**
     * This method prints the prompts to the screen.
     */
    public void print() {
        System.out.println(s);
    }
}
