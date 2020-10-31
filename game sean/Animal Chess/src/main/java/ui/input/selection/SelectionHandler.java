package ui.input.selection;

import ui.input.KeyHandler;

/**
 * Can be selected by the user.  Has actions defined for all directions, as well as for focus and selection.
 */
public interface SelectionHandler
{
    /**
     * Called when the user signals to move forward in the primary direction.
     * @param current The current KeyHandler.
     * @return The KeyHandler to return (could be the next element in the direction, or next option in the direction, etc).
     */
    KeyHandler primaryNext(KeyHandler current);
    
    /**
     * Called when the user signals to move backwards in the primary direction.
     * @param current The current KeyHandler.
     * @return The KeyHandler to return (could be the next element in the direction, or next option in the direction, etc).
     */
    KeyHandler primaryPrevious(KeyHandler current);
    
    /**
     * Called when the user signals to move forwards in the secondary direction.
     * @param current The current KeyHandler.
     * @return The KeyHandler to return (could be the next element in the direction, or next option in the direction, etc).
     */
    KeyHandler secondaryNext(KeyHandler current);
    
    /**
     * Called when the user signals to move backwards in the secondary direction.
     * @param current The current KeyHandler.
     * @return The KeyHandler to return (could be the next element in the direction, or next option in the direction, etc).
     */
    KeyHandler secondaryPrevious(KeyHandler current);
    
    /**
     * Called when the user selects this element.
     * @param current The current KeyHandler.
     * @return The KeyHandler to return (could be a different key handler for e.g. entering text).
     */
    KeyHandler select(KeyHandler current);
    
    /**
     * Called when this element is focused.
     * @param current The current KeyHandler.
     * @return The KeyHandler to return (normally this element's movement selection handler).
     */
    KeyHandler focus(KeyHandler current);
}
