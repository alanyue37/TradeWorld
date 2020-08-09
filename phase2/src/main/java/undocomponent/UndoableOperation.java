package undocomponent;

/**
 * A interface that undoable operations (i.e. commands in Command design pattern) must implement.
 * Inspired by explanations and code samples from:
 *  - https://sourcemaking.com/design_patterns/command
 *  - https://www.baeldung.com/java-command-pattern
 *  - https://en.wikipedia.org/wiki/Command_pattern#Java
 */
public interface UndoableOperation {

    /**
     * Reverses the operation that each concrete subclasses is responsible for and implements.
     * This method will throw NoLongerUndoableException if the operation is no longer undoable.
     * See the exception's documentation for more information.
     */
    void undo() throws NoLongerUndoableException;
}
