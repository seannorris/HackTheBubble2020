package ui.input.selection;

import ui.TerminalHandler;
import ui.drawing.DrawableSection;
import ui.input.EscapeHandler;
import ui.input.KeyHandler;

/**
 * Represents a selectable section that can be drawn to the terminal window.<br>
 * {@inheritDoc}
 */
public abstract class DrawableSelectableSection extends DrawableSection implements SelectableSection
{
    private boolean enabled;
    private KeyHandler handler;
    private SelectionHandler parent;
    
    /**
     * Constructor for DrawableSelectableSection with no underlying key handler.
     * @param enabled Boolean; true if section is enabled for selection.
     */
    public DrawableSelectableSection(boolean enabled)
    {
        this.enabled = enabled;
    }
    
    /**
     * Constructor for DrawableSelectableSection with an underlying key handler.
     * @param enabled Boolean; true if section is enabled for selection.
     * @param handler The underlying key handler.
     */
    public DrawableSelectableSection(boolean enabled, KeyHandler handler)
    {
        this(enabled);
        setHandler(handler);
    }
    
    /**
     * Constructor for DrawableSelectableSection with an underlying movement selection handler.
     * @param enabled Boolean; true if section is enabled for selection.
     * @param terminalHandler The TerminalHandler object.
     * @param escapeHandler The escape handler to use when escape is pressed.
     * @param horizontal Boolean; true if the primary direction is horizontal.
     */
    public DrawableSelectableSection(boolean enabled, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean horizontal)
    {
        this(enabled);
        setHandler(terminalHandler, escapeHandler, horizontal);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setHandler(KeyHandler handler)
    {
        this.handler = handler;
    }
    
    /**
     * Sets the underlying key handler to a new MovementSelectionHandler
     * @param terminalHandler The TerminalHandler object.
     * @param escapeHandler The escape handler to use when escape is pressed.
     * @param horizontal Boolean; true if the primary direction is horizontal.
     */
    public void setHandler(TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean horizontal)
    {
        setHandler(new MovementSelectionHandler<>(terminalHandler, escapeHandler, this, horizontal));
    }
    
    /**
     * Setter for enabled.
     * Unfocus if disabled.
     * @param enabled new enabled value.
     */
    public void setEnabled(boolean enabled)
    {
        if(!enabled)
            unfocus();
        
        this.enabled = enabled;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean enabled()
    {
        return enabled;
    }
    
    /**
     * {@inheritDoc}
     * In this case unfocuses and calls the parent's primaryNext method.
     * If there's no parent return the current key handler (i.e. do nothing).
     */
    @Override
    public KeyHandler primaryNext(KeyHandler current)
    {
        if(parent == null)
            return current;
    
        unfocus();
        return parent.primaryNext(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case unfocuses and calls the parent's primaryPrevious method.
     * If there's no parent return the current key handler (i.e. do nothing).
     */
    @Override
    public KeyHandler primaryPrevious(KeyHandler current)
    {
        if(parent == null)
            return current;
    
        unfocus();
        return parent.primaryPrevious(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case unfocuses and calls the parent's secondaryNext method.
     * If there's no parent return the current key handler (i.e. do nothing).
     */
    @Override
    public KeyHandler secondaryNext(KeyHandler current)
    {
        if(parent == null)
            return current;
    
        unfocus();
        return parent.secondaryNext(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case unfocuses and calls the parent's secondaryPrevious method.
     * If there's no parent return the current key handler (i.e. do nothing).
     */
    @Override
    public KeyHandler secondaryPrevious(KeyHandler current)
    {
        if(parent == null)
            return current;
        
        unfocus();
        return parent.secondaryPrevious(current);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(SelectionHandler parent)
    {
        this.parent = parent;
    }
    
    /**
     * {@inheritDoc}
     * If no handler is defined or section is not enabled call parent's focus method.
     * If no parent is defined return current key handler.
     */
    public KeyHandler focus(KeyHandler current)
    {
        return (!enabled || handler == null) ? ((parent != null ? parent.focus(current) : current)) : handler;
    }
    
    /**
     * Called whenever focus is lost (i.e. from moving to next selection).
     */
    protected abstract void unfocus();
    
    /**
     * {@inheritDoc}
     * In this case focus first.
     */
    @Override
    public KeyHandler getHandler()
    {
        return focus(handler);
    }
}
