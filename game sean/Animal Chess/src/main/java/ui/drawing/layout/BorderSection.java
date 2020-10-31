package ui.drawing.layout;

import ui.drawing.DrawableSection;
import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.drawing.colour.ColouredStringBuilder;

/**
 * Adds a border around another section.<br>
 * {@inheritDoc}
 * @param <T> The type of section to border.
 */
public class BorderSection<T extends Section> extends DrawableSection
{
    private final T section;
    private Border border;
    private Colour colour;
    private BackgroundColour backgroundColour;
    
    /**
     * Constructor for BorderSection.
     * @param section The section to border.
     * @param border The border type.
     * @param colour The foreground colour of the border.
     * @param backgroundColour The background colour of the border.
     */
    public BorderSection(T section, Border border, Colour colour, BackgroundColour backgroundColour)
    {
        this.border = border;
        this.section = section;
        this.colour = colour;
        this.backgroundColour = backgroundColour;
    }
    
    /**
     * Constructor for BorderSection with default colours.
     * @param section The section to border.
     * @param border The border type.
     */
    public BorderSection(T section, Border border)
    {
        this(section, border, Colour.DEFAULT, BackgroundColour.DEFAULT);
    }
    
    /**
     * Constructor for BorderSection with default background colour.
     * @param section The section to border.
     * @param border The border type.
     * @param colour The foreground colour of the border.
     */
    public BorderSection(T section, Border border, Colour colour)
    {
        this(section, border, colour, BackgroundColour.DEFAULT);
    }
    
    /**
     * Constructor for BorderSection with default foreground colour.
     * @param section The section to border.
     * @param border The border type.
     * @param backgroundColour The background colour of the border.
     */
    public BorderSection(T section, Border border, BackgroundColour backgroundColour)
    {
        this(section, border, Colour.DEFAULT, backgroundColour);
    }
    
    /**
     * Constructor for BorderSection.
     * @param section The section to border.
     * @param border The border type.
     * @param backgroundColour The background colour of the border.
     * @param colour The foreground colour of the border.
     */
    public BorderSection(T section, Border border, BackgroundColour backgroundColour, Colour colour)
    {
        this(section, border, colour, backgroundColour);
    }
    
    /**
     * Setter for the border type.
     * @param border The new border type.
     */
    public void setBorder(Border border)
    {
        this.border = border;
    }
    
    /**
     * Sets the foreground colour of the border.
     * @param colour The new foreground colour.
     */
    public void setColour(Colour colour)
    {
        this.colour = colour;
    }
    
    /**
     * Sets the background colour of the border.
     * @param backgroundColour The new background colour.
     */
    public void setBackgroundColour(BackgroundColour backgroundColour)
    {
        this.backgroundColour = backgroundColour;
    }
    
    /**
     * Getter for the underlying section.
     * @return The underlying section.
     */
    protected T getSection()
    {
        return section;
    }
    
    /**
     * Getter for border type.
     * @return The border type.
     */
    public Border getBorder()
    {
        return border;
    }
    
    /**
     * Return the foreground colour of the border.
     * @return The border's foreground colour.
     */
    public Colour getColour()
    {
        return colour;
    }
    
    /**
     * Return the background colour of the border.
     * @return The border's background colour.
     */
    public BackgroundColour getBackgroundColour()
    {
        return backgroundColour;
    }
    
    /**
     * {@inheritDoc}
     * In this case write the border characters around the underlying section's getRow result.
     * If it is the first or last row just draw border.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        var doLeft = startCol <= 0;
        var doRight = maxCols - startCol >= getColumns();
        var cols = Math.min(maxCols, getColumns() - startCol);
        
        if(doLeft)
        {
            doAppend(builder, row, true);
            cols--;
        }
        
        if(doRight)
            cols--;
        
        if(row == 0 || row == getRows() - 1)
        {
            var horizontal = getChar(border.getHorizontal());
            for(var x = 0; x < cols; x++)
                builder.append(horizontal);
        }
        else
            section.getRow(builder, row - 1, startCol < 1 ? startCol : startCol - 1, cols);
        
        if(doRight)
            doAppend(builder, row, false);
    }
    
    /**
     * Appends a border character from the first or last column.
     * @param builder ColouredStringBuilder to handle ANSI colour coding in the most efficient manner.
     * @param row Which row to generate.
     * @param right Boolean; true if in the rightmost column, false for leftmost.
     */
    private void doAppend(ColouredStringBuilder builder, int row, boolean right)
    {
        if(row == 0)
            builder.append(getChar(right ? border.getUpRight() : border.getUpLeft()));
        else if(row == getRows() -1)
            builder.append(getChar(right ? border.getDownRight() : border.getDownLeft()));
        else
            builder.append(getChar(border.getVertical()));
    }
    
    /**
     * Gets a coloured char from a character with the border's colours.
     * @param character The character to colour.
     * @return The coloured char.
     */
    private ColouredChar getChar(char character)
    {
        return new ColouredChar(character, colour, backgroundColour);
    }
    
    /**
     * {@inheritDoc}
     * In this case return the rows of the underlying section plus the height of the border characters.
     */
    @Override
    public int getRows()
    {
        return section.getRows() + 2;
    }
    
    /**
     * {@inheritDoc}
     * In this case return the columns of the underlying section plus the width of the border characters.
     */
    @Override
    public int getColumns()
    {
        return section.getColumns() + 2;
    }
    
    /**
     * {@inheritDoc}
     * In this case update the underlying section.
     */
    public void update()
    {
        section.update();
    }
    
    /**
     * Stores different possible border character sets.
     */
    public enum Border
    {
        STANDARD('─', '│', '┌', '┐', '└', '┘'),
        BOLD('━', '┃', '┏', '┓', '┗', '┛'),
        TRIPLE_DOTTED('┄', '┆', '┌', '┐', '└', '┘'),
        TRIPLE_DOTTED_BOLD('┅', '┇', '┏', '┓', '┗', '┛'),
        QUADRUPLE_DOTTED('┈', '┊', '┌', '┐', '└', '┘'),
        QUADRUPLE_DOTTED_BOLD('┉', '┋', '┏', '┓', '┗', '┛'),
        DOUBLE('═', '║', '╔', '╗', '╚', '╝'),
        BLOCK('█','█','█','█','█','█'),
        BLANK(FILLER_CHAR, FILLER_CHAR, FILLER_CHAR, FILLER_CHAR, FILLER_CHAR, FILLER_CHAR);
        
        private final char horizontal;
        private final char vertical;
        private final char upRight;
        private final char upLeft;
        private final char downRight;
        private final char downLeft;
    
        /**
         * Constructor for the Border enum.
         * @param horizontal The horizontal border character.
         * @param vertical The vertical border character.
         * @param upRight The top left corner border character.
         * @param upLeft The top right corner border character.
         * @param downRight The bottom left corner border character.
         * @param downLeft The bottom right corner border character.
         */
        Border(char horizontal, char vertical, char upRight, char upLeft, char downRight, char downLeft)
        {
            this.horizontal = horizontal;
            this.vertical = vertical;
            this.upRight = upRight;
            this.upLeft = upLeft;
            this.downRight = downRight;
            this.downLeft = downLeft;
        }
    
        /**
         * Getter for the horizontal border character.
         * @return The horizontal border character.
         */
        public char getHorizontal()
        {
            return horizontal;
        }
    
        /**
         * Getter for the vertical border character.
         * @return The vertical border character.
         */
        public char getVertical()
        {
            return vertical;
        }
    
        /**
         * Getter for the top left corner border character.
         * @return The top left corner border character.
         */
        public char getUpRight()
        {
            return upRight;
        }
    
        /**
         * Getter for the top right corner border character.
         * @return The top right corner border character.
         */
        public char getUpLeft()
        {
            return upLeft;
        }
    
        /**
         * Getter for the bottom left corner border character.
         * @return The bottom left corner border character.
         */
        public char getDownRight()
        {
            return downRight;
        }
    
        /**
         * Getter for the bottom right corner border character.
         * @return The bottom right corner border character.
         */
        public char getDownLeft()
        {
            return downLeft;
        }
    }
}
