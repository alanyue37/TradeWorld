package trademain;

import javafx.scene.Parent;

// TODO: explain what the purpose of this interface is in the javadocs
/**
 * An interface that all GUIs must implement.
 */
public interface RunnableGUI {

    void showScreen();

    Parent getRoot();

}
