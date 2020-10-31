package ui.drawing.composition;

import ui.drawing.Section;

import java.util.Map;

/**
 * Represents a collections of sections with positions that can be specified that are drawn together.<br>
 * {@inheritDoc}
 * @param <T> The type of sections that can be stored in the composer.
 */
public interface SectionComposer<T extends Section> extends Section
{
    /**
     * Adds a section at the specified coordinate.
     * @param section The section to add.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return Any section that was displaced.
     */
    T addSection(T section, int x, int y);
    
    /**
     * Adds a section at the specified coordinate.
     * @param section The section to add.
     * @param coord The coordinate, as a Coordinate object.
     * @return Any section that was displaced.
     */
    T addSection(T section, Coordinate coord);
    
    /**
     * Removes a section from the composer.
     * @param section The section to remove.
     * @return Boolean; true if section was removed.
     */
    boolean removeSection(T section);
    
    /**
     * Removes a section from the composer.
     * @param coord The coordinate to remove the section from.
     * @return Boolean; true if section was removed.
     */
    T removeSection(Coordinate coord);
    
    /**
     * Gets all the sections in the composer as a map of coordinate to section.
     * @return The map.
     */
    Map<? extends Coordinate, T> getSections();
}