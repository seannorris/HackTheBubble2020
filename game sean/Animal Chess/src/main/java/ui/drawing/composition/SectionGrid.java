package ui.drawing.composition;

import ui.drawing.DrawableSection;
import ui.drawing.Section;
import ui.drawing.colour.ColouredStringBuilder;
import ui.drawing.layout.ResponsiveSection;

/**
 * Contains a reference grid of sections in a section composer.<br>
 * {@inheritDoc}
 * @param <T> The type of sections that can be stored in the grid.
 * @param <U> The type of section composer to use.
 * @param <V> The type of sections that can be stored in the section composer.
 */
public abstract class SectionGrid<T extends V, U extends SectionComposer<V>, V extends Section> extends DrawableSection
{
    private final T[][] sections;
    private U composer;
    
    /**
     * Constructor for SectionGrid with custom section layout.
     * @param composer The section composer to use.
     * @param sections The grid of sections to store.
     */
    public SectionGrid(U composer, T[][] sections)
    {
        this.composer = composer;
        this.sections = sections;
    }
    
    /**
     * Constructor for SectionGrid with grid section layout.
     * Works out the maximum size of each row and column and creates a grid based on this.
     * If there are any responsive sections then resize them to the new size.
     * @param sections The grid of sections to store and process.
     */
    public SectionGrid(T[][] sections)
    {
        this.sections = sections;
        if(sections.length == 0 || sections[0].length == 0)
            return;
        
        var maxRowSize = new int[sections.length];
        var maxColSize = new int[sections[0].length];
        for(var row = 0; row < sections.length; row++)
        {
            for(var col = 0; col < sections[row].length; col++)
            {
                if(sections[row][col] == null)
                    continue;
                
                if(sections[row][col].getRows() > maxRowSize[row])
                    maxRowSize[row] = sections[row][col].getRows();
                if(sections[row][col].getColumns() > maxColSize[col])
                    maxColSize[col] = sections[row][col].getColumns();
            }
        }
        var cols = maxColSize[0];
        var rows = maxRowSize[0];
        var xCoords = new int[maxColSize.length];
        var yCoords = new int[maxRowSize.length];
        for(var x = 1; x < maxColSize.length || x < maxRowSize.length; x++)
        {
            if(x < maxColSize.length)
            {
                xCoords[x] = cols;
                cols += maxColSize[x];
            }
            if(x < maxRowSize.length)
            {
                yCoords[x] = rows;
                rows += maxRowSize[x];
            }
        }
        this.composer = getComposer(cols, rows);
        for(var row = 0; row < sections.length; row++)
        {
            for(var col = 0; col < sections[row].length; col++)
            {
                if(sections[row][col] == null)
                    continue;
                
                composer.addSection(sections[row][col], xCoords[col], yCoords[row]);
                if(sections[row][col] instanceof ResponsiveSection)
                    ((ResponsiveSection)sections[row][col]).setSize(maxRowSize[row], maxColSize[col]);
            }
        }
    }
    
    /**
     * Get a section composer instance from the implementation.
     * @param width The width to make the composer in characters.
     * @param height The height to make the composer in characters.
     * @return The section composer.
     */
    protected abstract U getComposer(int width, int height);
    
    /**
     * {@inheritDoc}
     * In this case pass through to the underlying section composer.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        composer.getRow(builder, row, startCol, maxCols);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the underlying section composer.
     */
    @Override
    public int getRows()
    {
        return composer.getRows();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the underlying section composer.
     */
    @Override
    public int getColumns()
    {
        return composer.getColumns();
    }
    
    /**
     * {@inheritDoc}
     * In this case update all components in the grid and the underlying composer.
     */
    public void update()
    {
        for(var row : sections)
            for(var section : row)
                if(section != null)
                    section.update();
        
        composer.update();
        super.update();
    }
    
    /**
     * Getter for the grid.
     * @return The grid of sections.
     */
    public T[][] getSections()
    {
        return sections;
    }
    
    /**
     * Getter for the underlying composer.
     * @return The underlying section composer.
     */
    public U getComposer()
    {
        return composer;
    }
}
