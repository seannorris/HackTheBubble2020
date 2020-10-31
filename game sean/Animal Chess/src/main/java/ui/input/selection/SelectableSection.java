package ui.input.selection;

import ui.drawing.Section;
import ui.input.KeyHandler;

/**
 * A section that is selectable (i.e. implements the SelectionHandler interface).
 */
public interface SelectableSection extends Section, SelectionHandler
{
    /**
     * Whether the section is enabled for selection or not.
     * @return Boolean; true if enabled.
     */
    boolean enabled();
    
    /**
     * Set the parent selection handler (meant to handle selection in a direction this section has no more options).
     * @param parent The new parent selection handler.
     */
    void setParent(SelectionHandler parent);
    
    /**
     * Sets the underlying KeyHandler.
     * @param handler The new KeyHandler.
     */
    void setHandler(KeyHandler handler);
    
    /**
     * Returns the underlying KeyHandler.
     * @return The KeyHandler.
     */
    KeyHandler getHandler();
}
