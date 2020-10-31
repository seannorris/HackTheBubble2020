package ui.drawing.colour;

import ui.drawing.BufferedSection;
import ui.drawing.DrawableSection;

/**
 * Represents a single coloured character.<br>
 * {@inheritDoc}
 */
public class ColouredChar extends DrawableSection implements BufferedSection, ColouredElement
{
    private Colour colour;
    private BackgroundColour backgroundColour;
    private char character;
    
    /**
     * Constructor for ColouredChar with default colours.
     * @param character The character to store.
     */
    public ColouredChar(char character)
    {
        this(character, Colour.DEFAULT, BackgroundColour.DEFAULT);
    }
    
    /**
     * Constructor for ColouredChar with a foreground colour specified.
     * @param character The character to store.
     * @param colour The colour to store.
     */
    public ColouredChar(char character, Colour colour)
    {
        this(character, colour, BackgroundColour.DEFAULT);
    }
    
    /**
     * Constructor for ColouredChar with a background colour specified.
     * @param character The character to store.
     * @param backgroundColour The background colour to store.
     */
    public ColouredChar(char character, BackgroundColour backgroundColour)
    {
        this(character, Colour.DEFAULT, backgroundColour);
    }
    
    /**
     * Constructor for ColouredChar with both a background colour and a foreground colour specified.
     * @param character The character to store.
     * @param backgroundColour The background colour to store.
     * @param colour The colour to store.
     */
    public ColouredChar(char character, BackgroundColour backgroundColour, Colour colour)
    {
        this(character, colour, backgroundColour);
    }
    
    /**
     * Constructor for ColouredChar with both a foreground colour and a background colour specified.
     * @param character The character to store.
     * @param colour The colour to store.
     * @param backgroundColour The background colour to store.
     */
    public ColouredChar(char character, Colour colour, BackgroundColour backgroundColour)
    {
        this.character = character;
        this.colour = colour;
        this.backgroundColour = backgroundColour;
    }
    
    /**
     * Shortcut to get a coloured char from a regular char.
     * @param in The character to store.
     * @return The new ColouredChar object.
     */
    public static ColouredChar of(char in)
    {
        return new ColouredChar(in);
    }
    
    /**
     * {@inheritDoc}
     */
    public Colour getColour()
    {
        return colour;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setColour(Colour colour)
    {
        this.colour = colour;
    }
    
    /**
     * {@inheritDoc}
     */
    public BackgroundColour getBackgroundColour()
    {
        return backgroundColour;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setBackgroundColour(BackgroundColour backgroundColour)
    {
        this.backgroundColour = backgroundColour;
    }
    
    /**
     * Getter for the stored character.
     * @return Stored char.
     */
    public char getChar()
    {
        return character;
    }
    
    /**
     * Setter for the stored character.
     * @param character Character to store.
     */
    public void setChar(char character)
    {
        this.character = character;
    }
    
    /**
     * {@inheritDoc}
     * In this case just append this coloured char.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        if(maxCols > 0)
            builder.append(this);
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
     * In this case just return 1.
     */
    @Override
    public int getColumns()
    {
        return 1;
    }
    
    /**
     * {@inheritDoc}
     * In this case just return a 1x1 array containing this coloured char.
     */
    @Override
    public ColouredChar[][] getBuffer()
    {
        return new ColouredChar[][]{new ColouredChar[]{this}};
    }
    
    /**
     * {@inheritDoc}
     * In this case just copy the first coloured character in the input section.
     */
    @Override
    public void copyBuffer(BufferedSection input, int row, int col)
    {
        var newChar = input.getBuffer()[0][0];
        this.character = newChar.character;
        this.colour = newChar.colour;
        this.backgroundColour = newChar.backgroundColour;
    }
    
    /**
     * {@inheritDoc}
     * In this case just return this coloured char.
     */
    @Override
    public ColouredChar getChar(int row, int col)
    {
        return this;
    }
    
    /**
     * Returns a copy of this coloured char.
     * @return The copy.
     */
    public ColouredChar copy()
    {
        return new ColouredChar(character, colour, backgroundColour);
    }
}
