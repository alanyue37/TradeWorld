package undocomponent;

import tradegateway.ObservableDataModel;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UndoManager implements Serializable {
    private final Map<String, UndoableOperation> undoableOperations; // id, undoableOperation pairs
    private final AtomicInteger counter = new AtomicInteger();
    private final ObservableDataModel observableDataModel;

    /**
     * Instantiates an UndoManager
     */
    public UndoManager(ObservableDataModel observableDataModel) {
        this.undoableOperations = new HashMap<>();
        this.observableDataModel = observableDataModel;

    }

    /**
     * Adds given undoableOperation to collection of outstanding UndoableOperations
     * @param undoableOperation An UndoableOperation which may be reversed at a later time
     */
    public void add(UndoableOperation undoableOperation) {
        String id = String.valueOf(counter.getAndIncrement());
        undoableOperations.put(id, undoableOperation);
        observableDataModel.setChanged();
    }

    /**
     * Executes the undo functionality of the UndoableOperation with the given undoId if it's contained in collection
     * of outstanding UndoableOperations.
     *
     * @param undoId Id of the UndoableOperation to be reversed
     * @throws NoLongerUndoableException the operation is no longer undoable
     */
    public void execute(String undoId) throws NoLongerUndoableException {
        if (undoableOperations.containsKey(undoId)) {
            undoableOperations.get(undoId).undo();
            undoableOperations.remove(undoId);
            observableDataModel.setChanged();
        }
    }

    /**
     * Returns collection of outstanding UndoableOperations in form of Map where each key is a undoId and the value
     * is the corresponding UndoableOperation.
     * @return Map containing all outstanding UndoableOperations mapped to their respective undoIds
     */
    public Map<String, UndoableOperation> getUndoableOperations() {
        return undoableOperations;
    }

    /**
     * Returns the UndoableOperation with given undoId
     *
     * @param undoId id of request UndoableOperation
     * @return UndoableOperation instance with the given id
     */
    public UndoableOperation getUndoableOperation(String undoId) {
        return undoableOperations.get(undoId);
    }
}
