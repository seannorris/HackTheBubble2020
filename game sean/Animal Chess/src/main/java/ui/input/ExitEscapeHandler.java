package ui.input;

import ui.TerminalHandler;

/**
 * An escape handler that exits the program.
 */
public class ExitEscapeHandler implements EscapeHandler
{
    private final TerminalHandler handler;
    
    /**
     * Constructor for ExitEscapeHandler.
     * @param handler The TerminalHandler to exit from.
     */
    public ExitEscapeHandler(TerminalHandler handler)
    {
        this.handler = handler;
    }
    
    /**
     * {@inheritDoc}
     * In this case set the exit property of the TerminalHandler to true.
     */
    @Override
    public KeyHandler escapePressed()
    {
        handler.exit();
        return null;
    }
}
