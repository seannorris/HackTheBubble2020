package ui.menu.button;

import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.input.KeyHandler;

/**
 * A button that exits the program.
 * This is done by setting the exit property of the TerminalHandler to true.<br>
 * {@inheritDoc}
 */
public class ExitButton extends Button
{
    private final TerminalHandler handler;
    
    /**
     * Constructor for ExitButton.
     * @param handler The TerminalHandler.
     * @param text The button text.
     * @param textColour The button text colour.
     * @param bracketColour The colour of the brackets that indicate this is a button.
     * @param unfocusedColour The background colour when unfocused.
     * @param focusedColour The background colour when focused.
     * @param enabled Boolean; true if button is enabled for selection.
     */
    public ExitButton(TerminalHandler handler, String text, Colour textColour, Colour bracketColour, BackgroundColour unfocusedColour, BackgroundColour focusedColour, boolean enabled)
    {
        super(text, textColour, bracketColour, unfocusedColour, focusedColour, enabled);
        this.handler = handler;
    }
    
    /**
     * {@inheritDoc}
     * In this case, exit the program.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        handler.exit();
        return null;
    }
}
