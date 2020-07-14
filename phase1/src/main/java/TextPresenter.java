import java.util.List;

public class TextPresenter {

    /**
     * Print message asking user to try again when input is invalid
     */
    public void tryAgain() {
        System.out.println("Invalid input. Please enter again:");
    }

    /**
     * Print message asking user to try again when input is invalid
     * @param numbered Number each item starting with 1 iff true
     * @param blankLines Insert a blank line after each item iff true
     */
    public void printList(List<String> items, boolean numbered, boolean blankLines) {
        String lineEnding = "";
        if (blankLines) {
            lineEnding = "\n";
        }
        if (numbered) {
            int i = 1;
            for (String s : items) {
                System.out.println(i + ". " + s + lineEnding);
                i++;
            }
        } else {
            for (String s : items) {
                System.out.println(s + lineEnding);
            }
        }
    }
}
