package ui.drawing.composition;

import ui.drawing.Section;

import java.util.Map;

/**
 * Provides a z index to give certain sections priority over others.<br>
 * {@inheritDoc}
 * @param <T> The type of sections that can be stored in the composer.
 */
public interface OrderedSectionComposer<T extends Section> extends SectionComposer<T>
{
    /**
     * Adds a section at the specified coordinate.
     * @param section The section to add.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z index.
     * @return Any section that was displaced.
     */
    T addSection(T section, int x, int y, int z);
    
    /**
     * {@inheritDoc}
     */
    T addSection(T section, OrderedCoordinate coord);
    
    /**
     * {@inheritDoc}
     */
    T removeSection(OrderedCoordinate coord);
    
    /**
     * {@inheritDoc}
     */
    Map<OrderedCoordinate, T> getSections();
}
