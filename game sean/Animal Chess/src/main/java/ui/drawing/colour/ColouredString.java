package ui.drawing.colour;

import ui.drawing.BufferedSection;
import ui.drawing.DrawableSection;

/**
 * Represents a single-colour coloured string.<br>
 * {@inheritDoc}
 */
public class ColouredString extends DrawableSection implements BufferedSection, ColouredElement
{
    private String string;
    private Colour colour;
    private BackgroundColour backgroundColour;
    
    /**
     * Constructor for ColouredString with both a foreground colour and a background colour specified.
     * @param string The string to store.
     * @param colour The colour to store.
     * @param backgroundColour The background colour to store.
     */
    public ColouredString(String string, Colour colour, BackgroundColour backgroundColour)
    {
        this.string = string;
        this.colour = colour;
        this.backgroundColour = backgroundColour;
    }
    
    /**
     * Constructor for ColouredString with default colours.
     * @param string The string to store.
     */
    public ColouredString(String string)
    {
        this(string, Colour.DEFAULT, BackgroundColour.DEFAULT);
    }
    
    /**
     * Constructor for ColouredString with a background colour specified.
     * @param string The string to store.
     * @param backgroundColour The background colour to store.
     */
    public ColouredString(String string, BackgroundColour backgroundColour)
    {
        this(string, Colour.DEFAULT, backgroundColour);
    }
    
    /**
     * Constructor for ColouredString with a foreground colour specified.
     * @param string The string to store.
     * @param colour The colour to store.
     */
    public ColouredString(String string, Colour colour)
    {
        this(string, colour, BackgroundColour.DEFAULT);
    }
    
    /**
     * Constructor for ColouredString with both a background colour and a foreground colour specified.
     * @param string The string to store.
     * @param backgroundColour The background colour to store.
     * @param colour The colour to store.
     */
    public ColouredString(String string, BackgroundColour backgroundColour, Colour colour)
    {
        this(string, colour, backgroundColour);
    }
    
    /**
     * {@inheritDoc}
     * In this case return an array containing each character of the string.
     */
    @Override
    public ColouredChar[][] getBuffer()
    {
        var out = new ColouredChar[1][string.length()];
        for(var x = 0; x < string.length(); x++)
            out[0][x] = new ColouredChar(string.charAt(x), colour, backgroundColour);
        
        return out;
    }
    
    /**
     * {@inheritDoc}
     * In this case copy the colour of the first element and the characters of the top row.
     */
    @Override
    public void copyBuffer(BufferedSection input, int row, int col)
    {
        var newText = new StringBuilder();
        newText.append(string);
        for(var x = 0; x < input.getBuffer()[0].length; x++)
            newText.append(input.getBuffer()[0][x].getChar());
        
        string = newText.toString();
        setColour(input.getBuffer()[0][0].getColour());
        setBackgroundColour(input.getBuffer()[0][0].getBackgroundColour());
    }
    
    /**
     * {@inheritDoc}
     * In this case return a coloured string of the character at the specified column.
     */
    @Override
    public ColouredChar getChar(int row, int col)
    {
        return new ColouredChar(string.charAt(col), colour, backgroundColour);
    }
    
    /**
     * {@inheritDoc}
     * In this case add the string to the builder, or if a startCol or maxCols is specified return the appropriate substring.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        if(maxCols >= string.length() && startCol == 0)
            builder.append(this);
        else
            builder.append(this.substring(startCol, Math.min(maxCols, string.length())));
    }
    
    /**
     * Returns a substring of the coloured string.
     * Works similarly to the substring method of the string class ({@link String#substring(int, int)}).
     * @param start The first index to include in the substring.
     * @param end The next index after the last index to include in the substring.
     * @return The coloured substring.
     */
    public ColouredString substring(int start, int end)
    {
        return new ColouredString(string.substring(start, end), colour, backgroundColour);
    }
    
    /**
     * {@inheritDoc}
     * In this case just return 1.
     */
    @Override
    public int getRows()
    {
        return 1;
    }
    
    /**
     * {@inheritDoc}
     * In this case return the length of the string.
     */
    @Override
    public int getColumns()
    {
        return string.length();
    }
    
    /**
     * Setter for the stored string.
     * @param string The new string to store.
     */
    public void setString(String string)
    {
        this.string = string;
    }
    
    /**
     * Getter for the stored string.
     * @return The stored string.
     */
    public String getString()
    {
        return string;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Colour getColour()
    {
        return colour;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setColour(Colour colour)
    {
        this.colour = colour;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setBackgroundColour(BackgroundColour backgroundColour)
    {
        this.backgroundColour = backgroundColour;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BackgroundColour getBackgroundColour()
    {
        return backgroundColour;
    }
}
