package ui.drawing.composition;

import ui.drawing.Section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Does not allow overlapping sections to be added.<br>
 * {@inheritDoc}
 * @param <T> The type of sections that can be stored in the composer.
 */
public class NoOverlapSectionComposer<T extends Section> extends BasicSectionComposer<T>
{
    /**
     * Constructor for NoOverlapSectionComposer.
     * A tree map is used to order the sections by x then y coordinate.
     * @param width The width of the composer in characters.
     * @param height The height of the composer in characters.
     */
    public NoOverlapSectionComposer(int width, int height)
    {
        super(width, height);
    }
    
    /**
     * {@inheritDoc}
     * But in this case an unmodifiable version.
     */
    @Override
    public Map<Coordinate, T> getSections()
    {
        return Collections.unmodifiableMap(super.getSections());
    }
    
    /**
     * {@inheritDoc}
     * But in this case verify that the section does not intersect with any existing sections.
     */
    @Override
    public T addSection(T newSection, Coordinate newCoord)
    {
        var sections = super.getSections();
        for(var coord : sections.keySet())
            if(intersects(newSection, newCoord, coord))
                return newSection;
        
        return super.addSection(newSection, newCoord);
    }
    
    /**
     * Adds a section to the underlying map, removing and returning a list of sections that intersect with it.
     * @param newSection The section to add.
     * @param newCoord The coordinate to add the section at, as a Coordinate object.
     * @return The list of displaced sections.
     */
    public List<T> forceAddSection(T newSection, Coordinate newCoord)
    {
        var sections = super.getSections();
        List<Coordinate> removed = null;
        for(var coord : sections.keySet())
        {
            if(!intersects(newSection, newCoord, coord))
                continue;
        
            if(removed == null)
                removed = new ArrayList<>();
            
            removed.add(coord);
        }
        List<T> out = null;
        if(removed != null)
        {
            out = new ArrayList<>();
            for(var coord : removed)
                out.add(sections.remove(coord));
        }
        super.addSection(newSection, newCoord);
        return out;
    }
    
    /**
     * Returns true if the piece at a specified position will intersect with a new piece at a new position.
     * @param newSection The new section to be added.
     * @param newCoord The position where the new section will be placed.
     * @param oldCoord The position of the old section to check.
     * @return Boolean; true if the sections will intersect.
     */
    private boolean intersects(Section newSection, Coordinate newCoord, Coordinate oldCoord)
    {
        var sections = super.getSections();
        if(oldCoord.getX() >= newCoord.getX() + newSection.getColumns() || oldCoord.getY() >= newCoord.getY() + newSection.getRows())
            return false;
    
        var section = sections.get(oldCoord);
        return newCoord.getX() < oldCoord.getX() + section.getColumns() && newCoord.getY() < oldCoord.getY() + section.getRows();
    }
}
