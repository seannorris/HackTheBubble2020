package ui.input.selection;

import ui.drawing.layout.ResponsiveSection;
import ui.input.selection.layout.SelectableResponsiveSection;

/**
 * An unselectable responsive section.<br>
 * {@inheritDoc}
 * @param <T> The type of underlying responsive section to store.
 */
public class UnselectableResponsiveSection<T extends ResponsiveSection> extends UnselectableSection<T> implements SelectableResponsiveSection
{
    /**
     * Constructor for UnselectableResponsiveSection
     * @param section The underlying section.
     */
    public UnselectableResponsiveSection(T section)
    {
        super(section);
    }
    
    /**
     * {@inheritDoc}
     * In this case pass through to underlying section.
     */
    @Override
    public void setSize(int rows, int columns)
    {
        getSection().setSize(rows, columns);
    }
}
