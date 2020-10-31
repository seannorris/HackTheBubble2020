package ui.drawing.composition;

import ui.drawing.BufferedSection;
import ui.drawing.DrawableBufferedSection;
import ui.drawing.Section;
import ui.drawing.colour.ColouredChar;

import java.util.Map;
import java.util.TreeMap;

/**
 * Stores a set of buffered sections and draws them in order to the buffer.
 * The order is determined first by the z-index and then by the coordinate; top left to bottom right.<br>
 * {@inheritDoc}
 * @param <T> The type of sections that can be stored in the composer.
 */
public class BufferedSectionComposer<T extends BufferedSection> extends DrawableBufferedSection implements OrderedSectionComposer<T>
{
    private final Map<OrderedCoordinate, T> sections;
    
    /**
     * Constructor for BufferedSectionComposer.
     * A tree map is used to order the sections by z index then x and y coordinates.
     * @param width The width of the composer in characters.
     * @param height The height of the composer in characters.
     * @param filler The character to fill the buffer with.
     */
    public BufferedSectionComposer(int width, int height, ColouredChar filler)
    {
        super(width, height, filler);
        sections = new TreeMap<>();
    }
    
    /**
     * Constructor for BufferedSectionComposer with default filler character.
     * @param width The width of the composer in characters.
     * @param height The height of the composer in characters.
     */
    public BufferedSectionComposer(int width, int height)
    {
        this(width, height, Section.FILLER);
    }
    
    /**
     * {@inheritDoc}
     * In this case just use underlying map's addSection method.
     */
    @Override
    public T addSection(T section, int x, int y, int z)
    {
        return addSection(section, OrderedCoordinate.at(x, y, z));
    }
    
    /**
     * {@inheritDoc}
     * In this case just use underlying map's addSection method.
     */
    @Override
    public T addSection(T section, OrderedCoordinate coord)
    {
        return sections.put(coord, section);
    }
    
    /**
     * {@inheritDoc}
     * In this case just use underlying map's remove method.
     */
    @Override
    public T removeSection(OrderedCoordinate coord)
    {
        return sections.remove(coord);
    }
    
    /**
     * {@inheritDoc}
     * In this case just use underlying map's add method.
     */
    @Override
    public T addSection(T section, int x, int y)
    {
        return addSection(section, OrderedCoordinate.at(x, y));
    }
    
    /**
     * {@inheritDoc}
     * In this case just use underlying map's add method.
     */
    @Override
    public T addSection(T section, Coordinate coord)
    {
        return addSection(section, OrderedCoordinate.at(coord));
    }
    
    /**
     * {@inheritDoc}
     * In this case just use underlying map's value collection's remove method.
     */
    @Override
    public boolean removeSection(T section)
    {
        return sections.values().remove(section);
    }
    
    /**
     * {@inheritDoc}
     * In this case just use underlying map's remove method.
     */
    @Override
    public T removeSection(Coordinate coord)
    {
        return removeSection(OrderedCoordinate.at(coord));
    }
    
    /**
     * {@inheritDoc}
     * In this case just return underlying map.
     */
    @Override
    public Map<OrderedCoordinate, T> getSections()
    {
        return sections;
    }
    
    /**
     * {@inheritDoc}
     * In this case call the update method of every contained section in order and copy their buffer into the buffer of this.
     */
    public void update()
    {
        for(var coord : sections.keySet())
        {
            var section = sections.get(coord);
            section.update();
            copyBuffer(section, coord.getY(), coord.getX());
        }
        super.update();
    }
}
