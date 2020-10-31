package ui.drawing.composition;

import ui.drawing.Section;

/**
 * Implementation for a BasicSectionComposer.<br>
 * {@inheritDoc}
 * @param <T> The type of sections that can be stored in the grid.
 * @param <U> The type of sections that can be stored in the section composer.
 */
public class BasicSectionGrid<T extends U, U extends Section> extends SectionGrid<T, BasicSectionComposer<U>, U>
{
    /**
     * Constructor for BasicSectionGrid with grid section layout.
     * Works out the maximum size of each row and column and creates a grid based on this.
     * If there are any responsive sections then resize them to the new size.
     * @param sections The grid of sections to store and process.
     */
    public BasicSectionGrid(T[][] sections)
    {
        super(sections);
    }
    
    /**
     * Constructor for BasicSectionGrid with custom section layout.
     * @param composer The section composer to use (a basic section composer).
     * @param sections The grid of sections to store.
     */
    public BasicSectionGrid(BasicSectionComposer<U> composer, T[][] sections)
    {
        super(composer, sections);
    }
    
    /**
     * {@inheritDoc}
     * In this case a BasicSectionComposer.
     */
    protected BasicSectionComposer<U> getComposer(int width, int height)
    {
        return new BasicSectionComposer<>(width, height);
    }
}
