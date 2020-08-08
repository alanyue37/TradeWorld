package undocomponent;

/**
 * This exception is thrown to indicate that an operation is no longer undoable.
 * Usually because one or more operations which occurred afterwards made it unreasonable to reverse.
 */
public class NoLongerUndoableException extends Exception {
    public NoLongerUndoableException() {
        super();
    }
}
