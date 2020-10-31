package ui.drawing.colour;

/**
 * Represents a coloured element, such as a coloured string or character.
 */
public interface ColouredElement
{
    /**
     * Return the foreground colour of the element.
     * @return The element's foreground colour.
     */
    Colour getColour();
    
    /**
     * Return the background colour of the element.
     * @return The element's background colour.
     */
    BackgroundColour getBackgroundColour();
    
    /**
     * Sets the foreground colour of the element.
     * @param colour The new foreground colour.
     */
    void setColour(Colour colour);
    
    /**
     * Sets the background colour of the element.
     * @param backgroundColour The new background colour.
     */
    void setBackgroundColour(BackgroundColour backgroundColour);
}
