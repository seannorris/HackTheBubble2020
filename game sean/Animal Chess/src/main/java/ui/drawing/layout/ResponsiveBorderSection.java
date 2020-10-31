package ui.drawing.layout;

import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.input.ResizeHandler;

/**
 * A bordered section that can implements the ResponsiveSection interface.
 * Also implements the ResizeHandler element.<br>
 * {@inheritDoc}
 * @param <T> The type of section to border.
 */
public class ResponsiveBorderSection<T extends ResponsiveSection> extends BorderSection<T> implements ResponsiveSection, ResizeHandler
{
    /**
     * Constructor for ResponsiveBorderSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     * @param colour The foreground colour of the border.
     * @param backgroundColour The background colour of the border.
     */
    public ResponsiveBorderSection(int rows, int columns, T section, Border border, Colour colour, BackgroundColour backgroundColour)
    {
        super(section, border, colour, backgroundColour);
        setSize(rows, columns);
    }
    
    /**
     * Constructor for ResponsiveBorderSection with default colours.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     */
    public ResponsiveBorderSection(int rows, int columns, T section, Border border)
    {
        this(rows, columns, section, border, Colour.DEFAULT, BackgroundColour.DEFAULT);
    }
    
    /**
     * Constructor for ResponsiveBorderSection with default background colour.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     * @param colour The foreground colour of the border.
     */
    public ResponsiveBorderSection(int rows, int columns, T section, Border border, Colour colour)
    {
        this(rows, columns, section, border, colour, BackgroundColour.DEFAULT);
    }
    
    /**
     * Constructor for ResponsiveBorderSection with default foreground colour.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     * @param backgroundColour The background colour of the border.
     */
    public ResponsiveBorderSection(int rows, int columns, T section, Border border, BackgroundColour backgroundColour)
    {
        this(rows, columns, section, border, Colour.DEFAULT, backgroundColour);
    }
    
    /**
     * Constructor for ResponsiveBorderSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     * @param backgroundColour The background colour of the border.
     * @param colour The foreground colour of the border.
     */
    public ResponsiveBorderSection(int rows, int columns, T section, Border border, BackgroundColour backgroundColour, Colour colour)
    {
        this(rows, columns, section, border, colour, backgroundColour);
    }
    
    /**
     * {@inheritDoc}
     * In this case set the size of the underlying responsive section to the size left inside the border.
     */
    @Override
    public void setSize(int rows, int columns)
    {
        getSection().setSize(rows - 2, columns - 2);
    }
    
    /**
     * {@inheritDoc}
     * In this case just call {@link #setSize(int, int)}.
     */
    @Override
    public void resize(int width, int height)
    {
        setSize(height, width);
    }
}
