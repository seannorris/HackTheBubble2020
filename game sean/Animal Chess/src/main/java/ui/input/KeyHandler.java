package ui.input;

import java.io.IOException;

/**
 * Handles key presses by the user.
 */
public interface KeyHandler
{
    /**
     * Processes a key press and returns the KeyHandler to process the next one.
     * @param key The key that was pressed.
     * @return The new KeyHandler.
     * @throws IOException Pass exception up chain.
     */
    KeyHandler keyPress(int key) throws IOException;
}
