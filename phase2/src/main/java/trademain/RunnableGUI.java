package trademain;

import javafx.scene.Parent;

/**
 * An interface that all GUIs must implement. Allows GUIs to created for each menu tab on log in and to show
 * the GUI on the screen.
 */
public interface RunnableGUI {

    /**
     * Sets the scene of the stage to this GUI.
     */
    void showScreen();

    /**
     * Returns the Parent consisting of contents of the GUI.
     * @return the Parent consisting of the contents of the GUI
     */
    Parent getRoot();

}
