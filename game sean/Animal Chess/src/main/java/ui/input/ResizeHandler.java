package ui.input;

/**
 * Handles when the user resizes the terminal window.
 */
public interface ResizeHandler
{
    /**
     * Processes the new terminal window size.
     * @param width The new width in characters.
     * @param height The new height in characters.
     */
    void resize(int width, int height);
}
