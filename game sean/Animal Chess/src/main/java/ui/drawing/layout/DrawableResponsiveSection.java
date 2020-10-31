package ui.drawing.layout;

import ui.drawing.DrawableSection;
import ui.input.ResizeHandler;

/**
 * A responsive section with a stored number of rows and columns.<br>
 * {@inheritDoc}
 */
public abstract class DrawableResponsiveSection extends DrawableSection implements ResponsiveSection, ResizeHandler
{
    private int rows;
    private int columns;
    
    /**
     * Constructor for DrawableResponsiveSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     */
    public DrawableResponsiveSection(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
    }
    
    /**
     * {@inheritDoc}
     * In this case return the stored number of rows.
     */
    @Override
    public int getRows()
    {
        return rows;
    }
    
    /**
     * {@inheritDoc}
     * In this case return the stored number of columns.
     */
    @Override
    public int getColumns()
    {
        return columns;
    }
    
    /**
     * {@inheritDoc}
     * In this case set the stored number of rows and columns.
     */
    public void setSize(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
    }
    
    /**
     * {@inheritDoc}
     * In this case just call {@link #setSize(int, int)}.
     */
    public void resize(int width, int height)
    {
        setSize(height, width);
    }
}
