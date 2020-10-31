package ui.game.menu;

import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.game.Piece;

/**
 * An ExitButton with defaults for use in a GameMenu.<br>
 * {@inheritDoc}
 */
public class ExitButton extends ui.menu.button.ExitButton
{
    public static final String BUTTON_TEXT = "Exit";
    
    /**
     * Constructor for ExitButton.
     * @param handler The TerminalHandler.
     * @param enabled Boolean; true if button is enabled for selection.
     */
    public ExitButton(TerminalHandler handler, boolean enabled)
    {
        super(handler, BUTTON_TEXT, Colour.DEFAULT, Colour.RED, BackgroundColour.DEFAULT, Piece.ENABLED_COLOUR, enabled);
    }
}
