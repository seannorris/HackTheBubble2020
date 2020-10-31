package ui.menu.button;

import ui.TerminalHandler;
import ui.drawing.PrintHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.input.KeyHandler;
import ui.input.ResizeHandler;
import ui.input.selection.SelectableSection;

/**
 * A button that changes to a different window.
 * This is done by changing the TerminalHandler's print, resize, and key handlers to the new window.<br>
 * {@inheritDoc}
 * @param <T> The type of selectable section to get the key handler from.
 * @param <U> The type of section to get the print and resize handlers from.
 */
public class ChangeViewButton<T extends SelectableSection, U extends PrintHandler & ResizeHandler> extends Button
{
    private T selectionHandler;
    private U container;
    private final TerminalHandler handler;
    
    /**
     * Constructor for ChangeViewButton without sections defined.
     * @param handler The TerminalHandler.
     * @param text The button text.
     * @param textColour The button text colour.
     * @param bracketColour The colour of the brackets that indicate this is a button.
     * @param unfocusedColour The background colour when unfocused.
     * @param focusedColour The background colour when focused.
     * @param enabled Boolean; true if button is enabled for selection.
     */
    public ChangeViewButton(TerminalHandler handler, String text, Colour textColour, Colour bracketColour, BackgroundColour unfocusedColour, BackgroundColour focusedColour, boolean enabled)
    {
        super(text, textColour, bracketColour, unfocusedColour, focusedColour, enabled);
        this.handler = handler;
    }
    
    /**
     * Constructor for ChangeViewButton.
     * @param selectionHandler The selectable section to get the key handler from.
     * @param container The section to get the print and resize handlers from.
     * @param handler The TerminalHandler.
     * @param text The button text.
     * @param textColour The button text colour.
     * @param bracketColour The colour of the brackets that indicate this is a button.
     * @param unfocusedColour The background colour when unfocused.
     * @param focusedColour The background colour when focused.
     * @param enabled Boolean; true if button is enabled for selection.
     */
    public ChangeViewButton(T selectionHandler, U container, TerminalHandler handler, String text, Colour textColour, Colour bracketColour, BackgroundColour unfocusedColour, BackgroundColour focusedColour, boolean enabled)
    {
        this(handler, text, textColour, bracketColour, unfocusedColour, focusedColour, enabled);
        this.selectionHandler = selectionHandler;
        this.container = container;
    }
    
    /**
     * Setter for the selectable section to get the key handler from.
     * @param selectionHandler The new selectable section.
     */
    public void setSelectionHandler(T selectionHandler)
    {
        this.selectionHandler = selectionHandler;
    }
    
    /**
     * Setter for the section to get the print and resize handlers from.
     * @param container The new section.
     */
    public void setContainer(U container)
    {
        this.container = container;
    }
    
    /**
     * {@inheritDoc}
     * In this case, change to the new view.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        handler.setPrintHandler(container);
        handler.setResizeHandler(container);
        return selectionHandler.getHandler();
    }
}
