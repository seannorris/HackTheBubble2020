package ui.menu.button;

import ui.drawing.colour.ColouredChar;
import ui.input.selection.layout.SelectableCentredSection;
import ui.menu.MenuItem;

/**
 * Represents a button in a menu.<br>
 * {@inheritDoc}
 * @param <T> The type of the underlying button.
 */
public class MenuButton<T extends Button> extends SelectableCentredSection<T> implements MenuItem
{
    private T button;
    
    /**
     * Constructor for MenuButton.
     * @param rows The number of rows to take up (centred vertically).
     * @param button The underlying button.
     * @param filler The filler to surround the button with.
     */
    public MenuButton(int rows, T button, ColouredChar filler)
    {
        super(rows, button.getColumns(), button, filler);
        this.button = button;
    }
    
    /**
     * Constructor for MenuButton with default filler.
     * @param rows The number of rows to take up (centred vertically).
     * @param button The underlying button.
     */
    public MenuButton(int rows, T button)
    {
        this(rows, button, FILLER);
    }
    
    /**
     * {@inheritDoc}
     * In this case return the width of the button.
     */
    @Override
    public int getContentWidth()
    {
        return button.getColumns();
    }
    
    /**
     * Getter for the underlying button.
     * @return The underlying button.
     */
    public T getButton()
    {
        return button;
    }
    
    /**
     * Setter for the underlying button.
     * @param button The new underlying button.
     */
    public void setButton(T button)
    {
        this.button = button;
        super.setSection(button);
    }
}
