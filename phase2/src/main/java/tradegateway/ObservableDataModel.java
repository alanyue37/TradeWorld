package tradegateway;

/**
 * Interface for classes which wish to be observable by classes that implement GUIObserver.
 * Primarily created for TradeModel.
 * Use this instead of Java's Observable where it is preferable to use an interface instead of a class for the observer.
 * Especially if the observer class is to inherit from another class.
 */
public interface ObservableDataModel {
    boolean hasChanged();
    void setChanged();
    void addObserver(GUIObserver observer);
    void clearObservers();
}