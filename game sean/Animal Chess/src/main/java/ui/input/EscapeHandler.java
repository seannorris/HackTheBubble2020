package ui.input;

/**
 * A special handler for when the user presses the escape key.
 */
public interface EscapeHandler
{
    /**
     * Called when the user presses escape.
     * @return The new KeyHandler. (can remain the same).
     */
    KeyHandler escapePressed();
}
