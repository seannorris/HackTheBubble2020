package ui.input.selection;

import ui.TerminalHandler;
import ui.input.EscapeHandler;
import ui.input.KeyHandler;
import ui.input.MovementKeyHandler;

/**
 * Translates key presses from the user into movement directions and selections.<br>
 * {@inheritDoc}
 * @param <T> The type of selection handler that will accept the commands.
 */
public class MovementSelectionHandler<T extends SelectionHandler> extends MovementKeyHandler
{
    private final boolean horizontal;
    private final T selectionHandler;
    
    /**
     * Constructor for MovementSelectionHandler.
     * @param handler The TerminalHandler object.
     * @param escapeHandler The escape handler to use when escape is pressed.
     * @param selectionHandler The selection handler that will handle the inputs.
     * @param horizontal Boolean; true if the primary direction is horizontal.
     */
    public MovementSelectionHandler(TerminalHandler handler, EscapeHandler escapeHandler, T selectionHandler, boolean horizontal)
    {
        super(handler, escapeHandler);
        this.selectionHandler = selectionHandler;
        this.horizontal = horizontal;
    }
    
    /**
     * {@inheritDoc}
     * In this case call the appropriate previous method of the selection handler, depending on the primary direction.
     */
    @Override
    protected KeyHandler moveUp()
    {
        return horizontal ? selectionHandler.secondaryPrevious(this) : selectionHandler.primaryPrevious(this);
    }
    
    /**
     * {@inheritDoc}
     * In this case call the appropriate next method of the selection handler, depending on the primary direction.
     */
    @Override
    protected KeyHandler moveDown()
    {
        return horizontal ? selectionHandler.secondaryNext(this) : selectionHandler.primaryNext(this);
    }
    
    /**
     * {@inheritDoc}
     * In this case call the appropriate previous method of the selection handler, depending on the primary direction.
     */
    @Override
    protected KeyHandler moveLeft()
    {
        return horizontal ? selectionHandler.primaryPrevious(this) : selectionHandler.secondaryPrevious(this);
    }
    
    /**
     * {@inheritDoc}
     * In this case call the appropriate next method of the selection handler, depending on the primary direction.
     */
    @Override
    protected KeyHandler moveRight()
    {
        return horizontal ? selectionHandler.primaryNext(this) : selectionHandler.secondaryNext(this);
    }
    
    /**
     * {@inheritDoc}
     * In this case call the select method of the selection handler.
     */
    @Override
    protected KeyHandler space()
    {
        return selectionHandler.select(this);
    }
    
    /**
     * {@inheritDoc}
     * In this case call the select method of the selection handler.
     */
    @Override
    protected KeyHandler enter()
    {
        return selectionHandler.select(this);
    }
    
    /**
     * {@inheritDoc}
     * In this case ignore the input.
     */
    @Override
    protected KeyHandler other(int key)
    {
        return this;
    }
}
