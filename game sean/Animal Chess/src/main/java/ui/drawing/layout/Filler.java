package ui.drawing.layout;

import ui.drawing.BufferedSection;
import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredElement;
import ui.drawing.colour.ColouredStringBuilder;

import java.util.Arrays;

/**
 * A responsive section that is all one coloured char.<br>
 * {@inheritDoc}
 */
public class Filler extends DrawableResponsiveSection implements BufferedSection, ColouredElement
{
    private final ColouredChar filler;
    
    /**
     * Constructor for filler.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param filler The filler character.
     */
    public Filler(int rows, int columns, ColouredChar filler)
    {
        super(rows, columns);
        this.filler = filler;
    }
    
    /**
     * Constructor for filler with default filler character.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     */
    public Filler(int rows, int columns)
    {
        this(rows, columns, new ColouredChar(Section.FILLER_CHAR, Colour.DEFAULT, BackgroundColour.DEFAULT));
    }
    
    /**
     * {@inheritDoc}
     * In this case just append the filler character the appropriate number of times.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        for(var x = 0; x < Math.min(getColumns() - startCol, maxCols); x++)
            builder.append(filler);
    }
    
    /**
     * {@inheritDoc}
     * In this case just return a new coloured char array filled with the filler character.
     */
    @Override
    public ColouredChar[][] getBuffer()
    {
        var row = new ColouredChar[getColumns()];
        Arrays.fill(row, filler);
        var out = new ColouredChar[getRows()][getColumns()];
        Arrays.fill(out, row);
        return out;
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public void copyBuffer(BufferedSection input, int row, int col) { }
    
    /**
     * {@inheritDoc}
     * In this case just return the filler character.
     */
    @Override
    public ColouredChar getChar(int row, int col)
    {
        return filler;
    }
    
    /**
     * {@inheritDoc}
     * In this case return the colour of the filler character.
     */
    @Override
    public Colour getColour()
    {
        return filler.getColour();
    }
    
    /**
     * {@inheritDoc}
     * In this case set the colour of the filler character.
     */
    @Override
    public void setColour(Colour colour)
    {
        filler.setColour(colour);
    }
    
    /**
     * {@inheritDoc}
     * In this case return the background colour of the filler character.
     */
    @Override
    public BackgroundColour getBackgroundColour()
    {
        return filler.getBackgroundColour();
    }
    
    /**
     * {@inheritDoc}
     * In this case set the background colour of the filler character.
     */
    @Override
    public void setBackgroundColour(BackgroundColour backgroundColour)
    {
        filler.setBackgroundColour(backgroundColour);
    }
}
