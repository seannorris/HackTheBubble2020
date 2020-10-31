package ui.drawing.layout;

import ui.drawing.Section;

/**
 * Represents a resizable section.<br>
 * {@inheritDoc}
 */
public interface ResponsiveSection extends Section
{
    /**
     * Sets the size of the section in rows and columns of characters.
     * @param rows The new number of rows.
     * @param columns The new number of columns.
     */
    void setSize(int rows, int columns);
}
