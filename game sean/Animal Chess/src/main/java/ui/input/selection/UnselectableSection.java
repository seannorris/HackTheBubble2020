package ui.input.selection;

import ui.drawing.DrawableSection;
import ui.drawing.Section;
import ui.drawing.colour.ColouredStringBuilder;
import ui.input.KeyHandler;

/**
 * Allows an unselectable section to be used like a selectable one.
 * Implements the SelectableSection interface with dummy values.<br>
 * {@inheritDoc}
 * @param <T> The type of underlying section to store.
 */
public class UnselectableSection<T extends Section> extends DrawableSection implements SelectableSection
{
    private final T section;
    
    private static final Handler HANDLER = new Handler();
    
    /**
     * Constructor for UnselectableSection
     * @param section The underlying section.
     */
    public UnselectableSection(T section)
    {
        this.section = section;
    }
    
    /**
     * {@inheritDoc}
     * In this case always return false.
     */
    @Override
    public boolean enabled()
    {
        return false;
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public void setParent(SelectionHandler parent)
    {
    
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public void setHandler(KeyHandler handler)
    {
    
    }
    
    /**
     * {@inheritDoc}
     * In this case return a dummy key handler.
     */
    @Override
    public KeyHandler getHandler()
    {
        return HANDLER;
    }
    
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        section.getRow(builder, row, startCol, maxCols);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public int getRows()
    {
        return section.getRows();
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public int getColumns()
    {
        return section.getColumns();
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public KeyHandler primaryNext(KeyHandler current)
    {
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public KeyHandler primaryPrevious(KeyHandler current)
    {
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public KeyHandler secondaryNext(KeyHandler current)
    {
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public KeyHandler secondaryPrevious(KeyHandler current)
    {
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case do nothing.
     */
    @Override
    public KeyHandler focus(KeyHandler current)
    {
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public void update()
    {
        section.update();
    }
    
    /**
     * Getter for underlying section.
     * @return The underlying section.
     */
    protected T getSection()
    {
        return section;
    }
    
    /**
     * Dummy key handler that does nothing.
     */
    protected static class Handler implements KeyHandler
    {
    
        /**
         * {@inheritDoc}
         * In this case do nothing.
         */
        @Override
        public KeyHandler keyPress(int key)
        {
            return this;
        }
    }
}
