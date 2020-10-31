package ui.drawing;

import ui.drawing.colour.ColouredChar;

/**
 * Represents a section that is based on a 2D buffer of coloured chars.<br>
 * {@inheritDoc}
 */
public interface BufferedSection extends Section
{
    /**
     * Gets the buffer.
     * @return The 2D coloured char buffer.
     */
    ColouredChar[][] getBuffer();
    
    /**
     * Copies another buffered section into this one at the specified position.
     * @param input The buffered section to copy.
     * @param row The row position.
     * @param col The column position.
     */
    void copyBuffer(BufferedSection input, int row, int col);
    
    /**
     * Get the character at a specified position in the buffer.
     * @param row The row position.
     * @param col The column position.
     * @return The coloured character.
     */
    ColouredChar getChar(int row, int col);
}
