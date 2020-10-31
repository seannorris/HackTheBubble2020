package ui.input.selection.layout;

import ui.drawing.colour.ColouredChar;
import ui.drawing.layout.CentredSection;
import ui.input.KeyHandler;
import ui.input.selection.SelectableSection;
import ui.input.selection.SelectionHandler;

/**
 * A selectable centred section.<br>
 * {@inheritDoc}
 * @param <T> The type of selectable section to centre.
 */
public class SelectableCentredSection<T extends SelectableSection> extends CentredSection<T> implements SelectableResponsiveSection
{
    /**
     * Constructor for SelectableCentredSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to centre.
     * @param filler The filler character.
     */
    public SelectableCentredSection(int rows, int columns, T section, ColouredChar filler)
    {
        super(rows, columns, section, filler);
    }
    
    /**
     * Constructor for SelectableCentredSection with default filler.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to centre.
     */
    public SelectableCentredSection(int rows, int columns, T section)
    {
        super(rows, columns, section);
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
