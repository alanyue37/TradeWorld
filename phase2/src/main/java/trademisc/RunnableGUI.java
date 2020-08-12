package trademisc;

import javafx.scene.Parent;

/**
 * An interface that all GUIs must implement.
 */
public interface RunnableGUI {

    void showScreen();

    Parent getRoot();

    void initialScreen(); // TODO: Consider deleting

}
