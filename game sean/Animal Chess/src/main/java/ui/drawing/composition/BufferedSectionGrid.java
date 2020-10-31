package ui.drawing.composition;

import ui.drawing.BufferedSection;

/**
 * Implementation for a BufferedSectionComposer.<br>
 * {@inheritDoc}
 * @param <T> The type of sections that can be stored in the grid.
 * @param <U> The type of sections that can be stored in the section composer.
 */
public class BufferedSectionGrid<T extends U, U extends BufferedSection> extends SectionGrid<T, BufferedSectionComposer<U>, U>
{
    /**
     * Constructor for BufferedSectionGrid with grid section layout.
     * Works out the maximum size of each row and column and creates a grid based on this.
     * If there are any responsive sections then resize them to the new size.
     * @param sections The grid of sections to store and process.
     */
    public BufferedSectionGrid(T[][] sections)
    {
        super(sections);
    }
    
    /**
     * Constructor for BufferedSectionGrid with custom section layout.
     * @param composer The section composer to use (a basic section composer).
     * @param sections The grid of sections to store.
     */
    public BufferedSectionGrid(BufferedSectionComposer<U> composer, T[][] sections)
    {
        super(composer, sections);
    }
    
    /**
     * {@inheritDoc}
     * In this case a BufferedSectionComposer.
     */
    @Override
    protected BufferedSectionComposer<U> getComposer(int width, int height)
    {
        return new BufferedSectionComposer<>(width, height);
    }
}
