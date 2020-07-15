/**
 * A class that prints the prompts from AdminPresenter. It is the text UI.
 */
public class AdminView {        // should this class be deleted since we have an interface already?
    public AdminPresenter s;

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
