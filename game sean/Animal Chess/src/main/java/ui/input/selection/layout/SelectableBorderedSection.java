package ui.input.selection.layout;

import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.layout.BorderSection;
import ui.input.KeyHandler;
import ui.input.selection.SelectableSection;
import ui.input.selection.SelectionHandler;

/**
 * A bordered selectable section.<br>
 * {@inheritDoc}
 * @param <T> The type of selectable section to border.
 */
public class SelectableBorderedSection<T extends SelectableSection> extends BorderSection<T> implements SelectableSection
{
    /**
     * Constructor for SelectableBorderSection.
     * @param section The section to border.
     * @param border The border type.
     * @param colour The foreground colour of the border.
     * @param backgroundColour The background colour of the border.
     */
    public SelectableBorderedSection(T section, Border border, Colour colour, BackgroundColour backgroundColour)
    {
        super(section, border, colour, backgroundColour);
    }
    
    /**
     * Constructor for SelectableBorderSection with default colours.
     * @param section The section to border.
     * @param border The border type.
     */
    public SelectableBorderedSection(T section, Border border)
    {
        super(section, border);
    }
    
    /**
     * Constructor for SelectableBorderSection with default background colour.
     * @param section The section to border.
     * @param border The border type.
     * @param colour The foreground colour of the border.
     */
    public SelectableBorderedSection(T section, Border border, Colour colour)
    {
        super(section, border, colour);
    }
    
    /**
     * Constructor for SelectableBorderSection with default foreground colour.
     * @param section The section to border.
     * @param border The border type.
     * @param backgroundColour The background colour of the border.
     */
    public SelectableBorderedSection(T section, Border border, BackgroundColour backgroundColour)
    {
        super(section, border, backgroundColour);
    }
    
    /**
     * Constructor for SelectableBorderSection.
     * @param section The section to border.
     * @param border The border type.
     * @param backgroundColour The background colour of the border.
     * @param colour The foreground colour of the border.
     */
    public SelectableBorderedSection(T section, Border border, BackgroundColour backgroundColour, Colour colour)
    {
        super(section, border, backgroundColour, colour);
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
