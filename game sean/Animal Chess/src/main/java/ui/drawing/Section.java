package ui.drawing;

import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredStringBuilder;

/**
 * Defines something that can be drawn to the terminal.
 * Has a width and height and a method that draws a specified row to a ColouredStringBuilder.
 */
public interface Section
{
    char FILLER_CHAR = ' ';
    ColouredChar FILLER = ColouredChar.of(FILLER_CHAR);
    
    /**
     * Generates a single line of the section to be written to the terminal.
     * @param builder ColouredStringBuilder to handle ANSI colour coding in the most efficient manner.
     * @param row Which row of the section to generate.
     * @param startCol The column of the section to start at.
     * @param maxCols The max number of columns to draw.
     */
    void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols);
    
    /**
     * Getter for number of rows.
     * @return The height of the section in characters.
     */
    int getRows();
    
    /**
     * Getter for number of columns.
     * @return The width of the section in characters.
     */
    int getColumns();
    
    /**
     * Called before drawing the section.
     */
    void update();
}
