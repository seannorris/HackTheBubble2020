package ui.drawing;

import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredStringBuilder;

/**
 * Represents a section that can be drawn to the terminal window.
 */
public abstract class DrawableSection implements Section, PrintHandler
{
    /**
     * An overloaded getRow method to get the whole row.
     * @param builder ColouredStringBuilder to handle ANSI colour coding in the most efficient manner.
     * @param row Which row of the section to generate.
     */
    public void getRow(ColouredStringBuilder builder, int row)
    {
        getRow(builder, row, 0, getColumns());
    }
    
    /**
     * {@inheritDoc}
     * In this case get each row of the section in turn using a coloured string builder and the {@link #getRow(ColouredStringBuilder, int) method.}
     */
    public String getDisplay()
    {
        update();
        
        var out = new ColouredStringBuilder();
        for(var x = 0; x < getRows(); x++)
        {
            if(x>0)
                out.newLine();
            
            getRow(out, x);
        }
        
        out.append(Colour.RESET);
        return out.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update() { }
}
