package ui.menu;

import ui.TerminalHandler;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.drawing.composition.BasicSectionComposer;
import ui.drawing.composition.BasicSectionGrid;
import ui.input.EscapeHandler;
import ui.input.selection.SelectableGrid;
import ui.input.selection.layout.SelectableCentredSection;
import ui.menu.button.Button;

/**
 * Represents a menu containing a list of selectable menu items.<br>
 * {@inheritDoc}
 */
public class Menu extends Dialog
{
    public static final int PADDING = 2;
    
    /**
     * Constructor for Menu.
     * @param menuItems The list of menu items.
     * @param buttons The buttons to show at the bottom of the menu.
     * @param terminalHandler The TerminalHandler.
     * @param escapeHandler The escape handler.
     * @param enabled Boolean; true if the menu is enabled for selection.
     * @param filler The filler character to use for the space around the buttons.
     * @param borderColour The foreground colour of the border.
     * @param borderBackgroundColour The background colour of the border.
     */
    public Menu(MenuItem[] menuItems, Button[] buttons, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean enabled, ColouredChar filler, Colour borderColour, BackgroundColour borderBackgroundColour)
    {
        super(createGrid(menuItems, terminalHandler, escapeHandler, enabled), buttons, terminalHandler, escapeHandler, enabled, filler, borderColour, borderBackgroundColour);
    }
    
    /**
     * Creates a new SelectableGrid based on a list of menu items.
     * Set all the menu items' sizes to the largest minimum size.
     * @param menuItems The list of menu items.
     * @param terminalHandler The TerminalHandler.
     * @param escapeHandler The escape handler.
     * @param enabled Boolean; true if the dialog is enabled for selection.
     * @return A selectable centred section containing the selectable grid.
     */
    private static SelectableCentredSection<SelectableGrid<MenuItem, BasicSectionComposer<MenuItem>, MenuItem>> createGrid(MenuItem[] menuItems, TerminalHandler terminalHandler, EscapeHandler escapeHandler, boolean enabled)
    {
        var maxWidth = 0;
        var height = 0;
        for(var menuItem : menuItems)
        {
            height += menuItem.getRows();
            maxWidth = Math.max(maxWidth, menuItem.getContentWidth());
        }
        
        if(menuItems.length > 0)
            menuItems[0].setSize(menuItems[0].getRows(), maxWidth);
        
        var grid = new MenuItem[menuItems.length][1];
        for(var x = 0; x < grid.length; x++)
            grid[x] = new MenuItem[]{menuItems[x]};
        return new SelectableCentredSection<>(height, maxWidth + 2 * PADDING,
                new SelectableGrid<>(new BasicSectionGrid<>(grid), terminalHandler, escapeHandler, enabled));
    }
}
