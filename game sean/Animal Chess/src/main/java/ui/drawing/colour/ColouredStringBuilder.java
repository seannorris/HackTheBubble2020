package ui.drawing.colour;

/**
 * Used to build a coloured output string in the most efficient way possible.
 * Stores the previous colour so ANSI codes only need to be printed on change of colour.
 */
public class ColouredStringBuilder
{
    private final StringBuilder builder;
    private Colour previousColour;
    private BackgroundColour previousBackgroundColour;
    
    /**
     * Constructor for ColouredStringBuilder.
     */
    public ColouredStringBuilder()
    {
        builder = new StringBuilder();
    }
    
    /**
     * Returns the underlying string builder.
     * @return The underlying string builder.
     */
    public StringBuilder getBuilder()
    {
        return builder;
    }
    
    /**
     * Appends a regular char.
     * Uses whatever colour was defined previously.
     * @param in The character to append.
     * @return this, to allow for chained appending.
     */
    public ColouredStringBuilder append(char in)
    {
        builder.append(in);
        return this;
    }
    
    /**
     * Appends a regular string.
     * Uses whatever colour was defined previously.
     * @param in The string to append.
     * @return this, to allow for chained appending.
     */
    public ColouredStringBuilder append(String in)
    {
        builder.append(in);
        return this;
    }
    
    /**
     * Appends a regular character array.
     * Uses whatever colour was defined previously.
     * @param in The char array to append.
     * @return this, to allow for chained appending.
     */
    public ColouredStringBuilder append(char[] in)
    {
        builder.append(in);
        return this;
    }
    
    /**
     * Appends a coloured char.
     * @param in The coloured char to append.
     * @return this, to allow for chained appending.
     */
    public ColouredStringBuilder append(ColouredChar in)
    {
        doColourUpdate(in);
        builder.append(in.getChar());
        return this;
    }
    
    /**
     * Appends a coloured string.
     * @param in The coloured string to append.
     * @return this, to allow for chained appending.
     */
    public ColouredStringBuilder append(ColouredString in)
    {
        doColourUpdate(in);
        builder.append(in.getString());
        return this;
    }
    
    /**
     * Checks if the specified ColouredElement has the same colour as the previous element.
     * If not, send an ANSI reset followed by the new colour.
     * @param in The coloured element to check.
     */
    private void doColourUpdate(ColouredElement in)
    {
        if(in.getColour() != previousColour|| in.getBackgroundColour() != previousBackgroundColour)
        {
            builder.append(Colour.RESET);
            previousColour = in.getColour();
            previousBackgroundColour = in.getBackgroundColour();
            builder.append(previousColour);
            builder.append(previousBackgroundColour);
        }
    }
    
    /**
     * Appends the new line string (\r\n or \n, depending on operating system).
     */
    public void newLine()
    {
        builder.append(System.lineSeparator());
    }
    
    /**
     * Appends an array of coloured chars in order.
     * @param in The array of coloured chars.
     * @return this, to allow for chained appending.
     */
    public ColouredStringBuilder append(ColouredChar[] in)
    {
        for(var colouredChar : in)
            append(colouredChar);
        
        return this;
    }
    
    /**
     * Returns the stored string from the underlying string builder, including ANSI codes.
     * @return The stored string.
     */
    @Override
    public String toString()
    {
        return builder.toString();
    }
}
