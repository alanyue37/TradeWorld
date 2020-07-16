package trademisc;

import java.util.List;

public class TextPresenter {

    /**
     * Prints message asking user to try again when input is invalid.
     */
    public void tryAgain() {
        System.out.println("Invalid input. Please enter again:");
    }

    /**
     * This method prints to the screen and signals to the user that the system has successfully ended.
     */
    public void end() {
        System.out.println("See you soon!");
    }

    /**
     * This method prints a string as an iterator or list item.
     * @param item to be printed.
     */
    public void listItem(String item) {
        System.out.println("\n- " + item);
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
