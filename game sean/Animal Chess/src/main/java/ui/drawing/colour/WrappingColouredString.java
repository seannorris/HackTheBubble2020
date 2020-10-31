package ui.drawing.colour;

import ui.drawing.DrawableBufferedSection;
import ui.drawing.Section;
import ui.drawing.layout.ResponsiveSection;

import java.util.Arrays;

/**
 * Renders a coloured string to a fixed size buffer, wrapping at the end of each row.<br>
 * {@inheritDoc}
 */
public class WrappingColouredString extends DrawableBufferedSection implements ColouredElement, ResponsiveSection
{
    private ColouredString string;
    
    /**
     * Constructor for WrappingColouredString.
     * @param string The coloured string to wrap.
     * @param rows The number of rows to make the buffer.
     * @param columns The number of columns to make the buffer.
     */
    public WrappingColouredString(ColouredString string, int rows, int columns)
    {
        super(Math.max(columns, 0), Math.max(rows, 0));
        this.string = string;
        populateBuffer();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Colour getColour()
    {
        return string.getColour();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BackgroundColour getBackgroundColour()
    {
        return string.getBackgroundColour();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setColour(Colour colour)
    {
        string.setColour(colour);
        populateBuffer();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setBackgroundColour(BackgroundColour backgroundColour)
    {
        string.setBackgroundColour(backgroundColour);
        populateBuffer();
    }
    
    /**
     * {@inheritDoc}
     * In this case recreate the buffer with the new size then populate it with the wrapped string.
     */
    @Override
    public void setSize(int rows, int columns)
    {
        setBuffer(new ColouredChar[Math.max(rows, 0)][Math.max(columns, 0)]);
        populateBuffer();
    }
    
    /**
     * Fits the coloured string inside the buffer.
     * If in the middle of a word wrap with a '-', otherwise just break on the space.
     * If max size exceeded truncate.
     */
    private void populateBuffer()
    {
        var buffer = getBuffer();
        int col = 0;
        int row = 0;
        for(int stringPos = 0; stringPos < string.getColumns() && row < buffer.length; stringPos++)
        {
            if(col == buffer[row].length - 1 && string.getChar(0, stringPos).getChar() != ' '
                    && (stringPos + 1 >= string.getColumns() || string.getChar(0, stringPos + 1).getChar() != ' '))
            {
                buffer[row][col] = new ColouredChar(string.getChar(0, stringPos - 1).getChar() == ' ' ? ' ' : '-',
                        string.getColour(), string.getBackgroundColour());
                stringPos--;
            }
            else
                buffer[row][col] = string.getChar(0, stringPos);
    
            if(col == buffer[row].length - 1 && stringPos + 1 < string.getColumns() && string.getChar(0, stringPos + 1).getChar() == ' ')
                stringPos++;
                
            col++;
            if(col >= buffer[row].length)
            {
                col = 0;
                row++;
            }
        }
        if(row < buffer.length && col < buffer[row].length)
        {
            var filler = new ColouredChar(Section.FILLER_CHAR, string.getColour(), string.getBackgroundColour());
            while(row < buffer.length)
            {
                if(col > 0)
                {
                    buffer[row][col] = filler;
                    col++;
                    if(col >= buffer[row].length)
                    {
                        col = 0;
                        row++;
                    }
                }
                else
                {
                    Arrays.fill(buffer[row], filler);
                    row++;
                }
            }
        }
    }
    
    /**
     * Getter for contained string (uncoloured).
     * @return The contained string.
     */
    public String getString()
    {
        return string.getString();
    }
    
    /**
     * Setter for the contained string (coloured)
     * @param string The new coloured string.
     */
    public void setString(ColouredString string)
    {
        this.string = string;
        populateBuffer();
    }
    
    /**
     * Setter for the contained string (uncoloured)
     * @param string The new string.
     */
    public void setString(String string)
    {
        this.string.setString(string);
        populateBuffer();
    }
}
