package tradegateway;

public interface ObservableDataModel {
    boolean hasChanged();
    void setChanged();
    void addObserver(GUIObserver observer);
    void clearObservers();
}