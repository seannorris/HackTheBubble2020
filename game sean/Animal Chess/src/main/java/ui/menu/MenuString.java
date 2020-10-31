package ui.menu;

import ui.drawing.colour.ColouredString;
import ui.drawing.layout.CentredSection;
import ui.input.selection.UnselectableResponsiveSection;

/**
 * Represents a coloured string wrapped in a centred section and an unselectable section.
 * Implements the MenuItem interface.<br>
 * {@inheritDoc}
 */
public class MenuString extends UnselectableResponsiveSection<CentredSection<ColouredString>> implements MenuItem
{
    /**
     * Constructor for MenuString.
     * @param string The underlying coloured string.
     * @param height The height to take up (centred vertically).
     */
    public MenuString(ColouredString string, int height)
    {
        super(new CentredSection<>(height, string.getColumns(), string));
    }
    
    /**
     * {@inheritDoc}
     * In this case just return the string width.
     */
    @Override
    public int getContentWidth()
    {
        return getColumns();
    }
}
