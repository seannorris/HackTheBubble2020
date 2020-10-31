package ui.input;

import ui.TerminalHandler;

import java.io.IOException;

/**
 * A key handler that translates key presses into movement directions.
 * Also detects enter, space, and escape keys.
 */
public abstract class MovementKeyHandler implements KeyHandler
{
    private final TerminalHandler handler;
    private final EscapeHandler escapeHandler;
    
    /**
     * Constructor for MovementKeyHandler.
     * @param handler The TerminalHandler object.
     * @param escapeHandler The escape handler to use when escape is pressed.
     */
    public MovementKeyHandler(TerminalHandler handler, EscapeHandler escapeHandler)
    {
        this.handler = handler;
        this.escapeHandler = escapeHandler;
    }
    
    /**
     * {@inheritDoc}
     * Detects the movement keys and selection keys.
     * Calls the appropriate abstract method depending on the action.
     * As arrow keys are entered as escape sequences the logic for escape checks the next character to make sure it is not an arrow.
     * If the user does press escape there won't be a next character instantly so the operation has a time limit.
     */
    @Override
    public KeyHandler keyPress(int key) throws IOException
    {
        switch (key)
        {
            case 27:
                var read = handler.getNextChar(50);
                if(read == 91 || read == 79)
                    return keyPress(handler.getNextChar(-1));
            
                return escapeHandler.escapePressed();
            case 119:
            case 65:
                return moveUp();
            case 97:
            case 68:
                return moveLeft();
            case 115:
            case 66:
                return moveDown();
            case 100:
            case 67:
                return moveRight();
            case 32:
                return space();
            case 13:
                return enter();
            default:
                return other(key);
        }
    }
    
    /**
     * Called when the user presses an up key (up arrow or w).
     * @return The new KeyHandler.
     */
    protected abstract KeyHandler moveUp();
    
    /**
     * Called when the user presses a down key (down arrow or s).
     * @return The new KeyHandler.
     */
    protected abstract KeyHandler moveDown();
    
    /**
     * Called when the user presses a left key (left arrow or a).
     * @return The new KeyHandler.
     */
    protected abstract KeyHandler moveLeft();
    
    /**
     * Called when the user presses a right key (right arrow or d).
     * @return The new KeyHandler.
     */
    protected abstract KeyHandler moveRight();
    
    /**
     * Called when the user presses the space key.
     * @return The new KeyHandler.
     */
    protected abstract KeyHandler space();
    
    /**
     * Called when the user presses the enter key.
     * @return The new KeyHandler.
     */
    protected abstract KeyHandler enter();
    
    /**
     * Called when the user presses another key.
     * @param key the key pressed.
     * @return The new KeyHandler.
     */
    protected abstract KeyHandler other(int key);
    
    /**
     * Getter for the TerminalHandler object.
     * @return The TerminalHandler.
     */
    protected TerminalHandler getHandler()
    {
        return handler;
    }
}
