package ui.drawing.layout;

import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredElement;
import ui.drawing.colour.ColouredStringBuilder;

/**
 * Centres a section by filling the characters around it with the filler char.<br>
 * {@inheritDoc}
 * @param <T> The type of section to centre.
 */
public class CentredSection<T extends Section> extends DrawableResponsiveSection implements ColouredElement
{
    private T section;
    private final ColouredChar filler;
    
    /**
     * Constructor for CentredSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to centre.
     * @param filler The filler character.
     */
    public CentredSection(int rows, int columns, T section, ColouredChar filler)
    {
        super(rows, columns);
        this.section = section;
        this.filler = filler;
    }
    
    /**
     * Constructor for CentredSection with default filler.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to centre.
     */
    public CentredSection(int rows, int columns, T section)
    {
        this(rows, columns, section, new ColouredChar(Section.FILLER_CHAR, Colour.DEFAULT, BackgroundColour.DEFAULT));
    }
    
    /**
     * {@inheritDoc}
     * In this case pad the row from the underlying section on both sides to centre it horizontally, and modify the row index / draw black rows to centre it vertically.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        var rows = Math.max(getRows(), section.getRows());
        var cols = Math.max(getColumns(), section.getColumns());
        var hpadding = (cols - section.getColumns());
        var vpadding = (rows - section.getRows());
        cols = Math.min(cols - startCol, maxCols);
        
        if(row < vpadding / 2 || row >= vpadding / 2 + section.getRows())
        {
            pad(builder, cols);
            return;
        }
        var lpadding = Math.max(hpadding / 2 - startCol, 0);
        var sectionStartCol = Math.max(startCol - hpadding / 2, 0);
        var rpadding = Math.min(hpadding - hpadding / 2,  Math.max(maxCols - lpadding - section.getColumns() + sectionStartCol, 0));
        var sectionCols = Math.min(section.getColumns() - sectionStartCol, maxCols - lpadding);
        pad(builder, lpadding);
        section.getRow(builder, row - vpadding / 2, sectionStartCol, sectionCols);
        pad(builder, rpadding);
    }
    
    /**
     * Appends the filler character a specified number of times to the specified ColouredStringBuilder.
     * @param builder The coloured string builder.
     * @param count The number of times to append.
     */
    private void pad(ColouredStringBuilder builder, int count)
    {
        for(var x = 0; x < count; x++)
            builder.append(filler);
    }
    
    /**
     * {@inheritDoc}
     * In this case update the underlying section.
     */
    public void update()
    {
        section.update();
    }
    
    /**
     * Getter for the underlying section.
     * @return The underlying section.
     */
    public T getSection()
    {
        return section;
    }
    
    /**
     * Setter for the underlying section.
     * @param section The new underlying section.
     */
    public void setSection(T section)
    {
        this.section = section;
    }
    
    /**
     * {@inheritDoc}
     * In this case the background filler colour.
     */
    @Override
    public Colour getColour()
    {
        return filler.getColour();
    }
    
    /**
     * {@inheritDoc}
     * In this case the background filler colour.
     */
    @Override
    public void setColour(Colour colour)
    {
        filler.setColour(colour);
    }
    
    /**
     * {@inheritDoc}
     * In this case the background filler colour.
     */
    @Override
    public BackgroundColour getBackgroundColour()
    {
        return filler.getBackgroundColour();
    }
    
    /**
     * {@inheritDoc}
     * In this case the background filler colour.
     */
    @Override
    public void setBackgroundColour(BackgroundColour backgroundColour)
    {
        filler.setBackgroundColour(backgroundColour);
    }
}
