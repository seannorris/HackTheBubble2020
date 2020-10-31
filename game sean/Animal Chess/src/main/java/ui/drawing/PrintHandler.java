package ui.drawing;

/**
 * Represents something that can return a fully formatted string to render a whole terminal window.
 */
public interface PrintHandler
{
    /**
     * Get the ANSI coded terminal screen.
     * @return The terminal window as a string.
     */
    String getDisplay();
}
