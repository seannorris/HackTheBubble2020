package ui.input.selection.layout;

import ui.TerminalHandler;
import ui.drawing.layout.MinimumSizeSection;
import ui.input.KeyHandler;
import ui.input.selection.SelectionHandler;

/**
 * A selectable minimum size section.<br>
 * {@inheritDoc}
 * @param <T> A common type between the two selectable sections.
 * @param <U> The type of the normal selectable section.
 * @param <V> The type of the selectable section shown when smaller than the minimum size.
 */
public class SelectableMinimumSizeSection<T extends SelectableResponsiveSection, U extends T, V extends T> extends MinimumSizeSection<T, U, V> implements SelectableResponsiveSection
{
    private final TerminalHandler handler;
    
    /**
     * Constructor for SelectableMinimumSizeSection.
     * @param rows The initial number of rows in characters.
     * @param columns The initial number of columns in characters.
     * @param section The section to display if the minimum size is satisfied.
     * @param tooSmallSection The section to display if the minimum size is not satisfied.
     * @param minimumRows The minimum number of rows in characters.
     * @param minimumColumns The minimum number of columns in characters.
     * @param handler The TerminalHandler.
     */
    public SelectableMinimumSizeSection(int rows, int columns, U section, V tooSmallSection, int minimumRows, int minimumColumns, TerminalHandler handler)
    {
        super(rows, columns, section, tooSmallSection, minimumRows, minimumColumns);
        this.handler = handler;
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public boolean enabled()
    {
        return currentSection().enabled();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public void setParent(SelectionHandler parent)
    {
        currentSection().setParent(parent);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public KeyHandler getHandler()
    {
        return currentSection().getHandler();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public void setHandler(KeyHandler handler)
    {
        currentSection().setHandler(handler);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public KeyHandler primaryNext(KeyHandler current)
    {
        return currentSection().primaryNext(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public KeyHandler primaryPrevious(KeyHandler current)
    {
        return currentSection().primaryPrevious(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public KeyHandler secondaryNext(KeyHandler current)
    {
        return currentSection().secondaryNext(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public KeyHandler secondaryPrevious(KeyHandler current)
    {
        return currentSection().secondaryPrevious(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        return currentSection().select(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to the currently selected section.
     */
    @Override
    public KeyHandler focus(KeyHandler current)
    {
        return currentSection().focus(current);
    }
    
    
    /**
     * {@inheritDoc}
     * Set the selected key handler to the newly selected section.
     */
    @Override
    public void setSize(int rows, int columns)
    {
        var oldSection = currentSection();
        super.setSize(rows, columns);
        if(!currentSection().equals(oldSection) && enabled())
            handler.setKeyHandler(getHandler());
    }
}
