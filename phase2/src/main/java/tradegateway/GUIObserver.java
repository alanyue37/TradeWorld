package tradegateway;

/**
 * An interface used for GUIs which wish to observe an Observer (as in Observer design pattern).
 * Primarily created for Observers of which implement ObservableDataModel interface which only accepts observers of
 * this kind.
 */
public interface GUIObserver {

    /**
     * Called by observable to indicate that it has changed and observer should query it for changed information.
     */
    void update();
}