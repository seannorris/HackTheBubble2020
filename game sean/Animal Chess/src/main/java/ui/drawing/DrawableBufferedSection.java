package ui.drawing;

import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredStringBuilder;

import java.util.Arrays;

/**
 * A buffered section implementation.<br>
 * {@inheritDoc}
 */
public class DrawableBufferedSection extends DrawableSection implements BufferedSection
{
    private ColouredChar[][] buffer;
    
    /**
     * Constructor for DrawableBufferedSection with pre-existing buffer.
     * @param buffer The buffer to use.
     */
    public DrawableBufferedSection(ColouredChar[][] buffer)
    {
        this.buffer = buffer;
    }
    
    /**
     * Constructor for DrawableBufferedSection.
     * @param width The width of the buffer in characters.
     * @param height The height of the buffer in characters.
     * @param filler The character to initially fill the buffer with.
     */
    public DrawableBufferedSection(int width, int height, ColouredChar filler)
    {
        buffer = new ColouredChar[height][width];
        for(ColouredChar[] colouredChars : buffer)
            Arrays.fill(colouredChars, filler);
    }
    
    /**
     * Constructor for DrawableBufferedSection with default filler.
     * @param width The width of the buffer in characters.
     * @param height The height of the buffer in characters.
     */
    public DrawableBufferedSection(int width, int height)
    {
        this(width, height, Section.FILLER);
    }
    
    /**
     *{@inheritDoc}
     * In this case return the stored buffer.
     */
    public ColouredChar[][] getBuffer()
    {
        return buffer;
    }
    
    /**
     * Setter for the buffer.
     * @param buffer The new buffer.
     */
    public void setBuffer(ColouredChar[][] buffer)
    {
        this.buffer = buffer;
    }
    
    /**
     * {@inheritDoc}
     */
    public ColouredChar getChar(int row, int col)
    {
        return buffer[row][col];
    }
    
    /**
     * Copies another 2D coloured char buffer to the specified position.
     * @param input The buffer to copy.
     * @param row The row position.
     * @param col The column position.
     */
    public void copyBuffer(ColouredChar[][] input, int row, int col)
    {
        var endRow = Math.min(row + input.length, buffer.length);
        var cols = Math.min(input[0].length, buffer[0].length - col);
        for(var x = row; x < endRow; x++)
            System.arraycopy(input[x - row], 0, buffer[x], col, cols);
    }
    
    /**
     * {@inheritDoc}
     * In this case use {@link #copyBuffer(ColouredChar[][], int, int)}.
     */
    public void copyBuffer(BufferedSection input, int row, int col)
    {
        copyBuffer(input.getBuffer(), row, col);
    }
    
    /**
     * {@inheritDoc}
     * In this case either append a whole row in one go or append part of a row character by character.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        if(maxCols >= buffer[row].length && startCol == 0)
            builder.append(buffer[row]);
        else
        {
            startCol = Math.min(startCol, Math.max(buffer[row].length - maxCols, 0));
            for(var x = startCol; x - startCol < maxCols && x < buffer[row].length; x++)
                builder.append(buffer[row][x]);
        }
    }
    
    /**
     * {@inheritDoc}
     * In this case return the height of the stored buffer.
     */
    @Override
    public int getRows()
    {
        return buffer.length;
    }
    
    /**
     * {@inheritDoc}
     * In this case return the width of the stored buffer.
     */
    @Override
    public int getColumns()
    {
        return buffer.length > 0 ? buffer[0].length : 0;
    }
}
