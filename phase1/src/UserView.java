/**
 * class UserView is the text UI
 */
public class UserView {
    private UserPresenter s;
    /**
     * @param s that the UserPresenter sends
     */
    public void print(String s) {
        System.out.println(s);
    }
}