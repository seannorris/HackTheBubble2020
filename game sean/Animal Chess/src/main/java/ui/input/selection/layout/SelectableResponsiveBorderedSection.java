package ui.input.selection.layout;

import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.layout.ResponsiveBorderSection;
import ui.input.KeyHandler;
import ui.input.selection.SelectionHandler;

/**
 * A selectable responsive bordered section.<br>
 * {@inheritDoc}
 * @param <T> The type of selectable responsive section to border.
 */
public class SelectableResponsiveBorderedSection<T extends SelectableResponsiveSection> extends ResponsiveBorderSection<T> implements SelectableResponsiveSection
{
    /**
     * Constructor for SelectableResponsiveBorderSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     * @param colour The foreground colour of the border.
     * @param backgroundColour The background colour of the border.
     */
    public SelectableResponsiveBorderedSection(int rows, int columns, T section, Border border, Colour colour, BackgroundColour backgroundColour)
    {
        super(rows, columns, section, border, colour, backgroundColour);
    }
    
    /**
     * Constructor for SelectableResponsiveBorderSection with default colours.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     */
    public SelectableResponsiveBorderedSection(int rows, int columns, T section, Border border)
    {
        super(rows, columns, section, border);
    }
    
    /**
     * Constructor for SelectableResponsiveBorderSection with default background colour.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     * @param colour The foreground colour of the border.
     */
    public SelectableResponsiveBorderedSection(int rows, int columns, T section, Border border, Colour colour)
    {
        super(rows, columns, section, border, colour);
    }
    
    /**
     * Constructor for SelectableResponsiveBorderSection with default foreground colour.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     * @param backgroundColour The background colour of the border.
     */
    public SelectableResponsiveBorderedSection(int rows, int columns, T section, Border border, BackgroundColour backgroundColour)
    {
        super(rows, columns, section, border, backgroundColour);
    }
    
    /**
     * Constructor for SelectableResponsiveBorderSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to border.
     * @param border The border type.
     * @param backgroundColour The background colour of the border.
     * @param colour The foreground colour of the border.
     */
    public SelectableResponsiveBorderedSection(int rows, int columns, T section, Border border, BackgroundColour backgroundColour, Colour colour)
    {
        super(rows, columns, section, border, backgroundColour, colour);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public boolean enabled()
    {
        return getSection().enabled();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public void setParent(SelectionHandler parent)
    {
        getSection().setParent(parent);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public void setHandler(KeyHandler handler)
    {
        getSection().setHandler(handler);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public KeyHandler getHandler()
    {
        return getSection().getHandler();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public KeyHandler primaryNext(KeyHandler current)
    {
        return getSection().primaryNext(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public KeyHandler primaryPrevious(KeyHandler current)
    {
        return getSection().primaryPrevious(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public KeyHandler secondaryNext(KeyHandler current)
    {
        return getSection().secondaryNext(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public KeyHandler secondaryPrevious(KeyHandler current)
    {
        return getSection().secondaryPrevious(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        return getSection().select(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public KeyHandler focus(KeyHandler current)
    {
        return getSection().focus(current);
    }
}
