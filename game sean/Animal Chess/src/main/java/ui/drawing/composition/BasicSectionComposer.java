package ui.drawing.composition;

import ui.drawing.Section;
import ui.drawing.colour.ColouredStringBuilder;
import ui.drawing.layout.DrawableResponsiveSection;

import java.util.Map;
import java.util.TreeMap;

/**
 * Stores a map of coordinates to sections and assembles each row dynamically using each section's getRow method and a filler character.
 * The sections should have priority by first x coordinate and then y coordinate.<br>
 * {@inheritDoc}
 * @param <T> The type of sections that can be stored in the composer.
 */
public class BasicSectionComposer<T extends Section> extends DrawableResponsiveSection implements SectionComposer<T>
{
    private final Map<Coordinate, T> sections;
    
    /**
     * Constructor for BasicSectionComposer.
     * A tree map is used to order the sections by x then y coordinate.
     * @param width The width of the composer in characters.
     * @param height The height of the composer in characters.
     */
    public BasicSectionComposer(int width, int height)
    {
        super(height, width);
        sections = new TreeMap<>();
    }
    
    /**
     * {@inheritDoc}
     * In this case just return underlying map.
     */
    public Map<Coordinate, T> getSections()
    {
        return sections;
    }
    
    /**
     * {@inheritDoc}
     * In this case just use map's addSection method.
     */
    public T addSection(T section, int x, int y)
    {
        return addSection(section, Coordinate.at(x, y));
    }
    
    /**
     * {@inheritDoc}
     * In this case just use map's addSection method.
     */
    public T addSection(T section, Coordinate coord)
    {
        return sections.put(coord, section);
    }
    
    /**
     * {@inheritDoc}
     * In this case just use map's value collection's remove() method.
     */
    @Override
    public boolean removeSection(T section)
    {
        return sections.values().remove(section);
    }
    
    /**
     * {@inheritDoc}
     * In this case just use map's remove() method.
     */
    @Override
    public T removeSection(Coordinate coord)
    {
        return sections.remove(coord);
    }
    
    /**
     * {@inheritDoc}
     * In this case check each x coordinate in turn, if a section has a character in this position append it, otherwise append the filler character.
     */
    @Override
    public void getRow(ColouredStringBuilder builder, int row, int startCol, int maxCols)
    {
        var endCol = Math.min(startCol + maxCols, getColumns());
        var col = startCol;
        for(var coord : sections.keySet())
        {
            while(coord.getX() > col && col < endCol)
            {
                builder.append(FILLER);
                col++;
            }
            if(col >= endCol)
                break;
            
            var section = sections.get(coord);
            if(coord.getY() <= row && coord.getY() + section.getRows() > row && coord.getX() + section.getColumns() > col)
            {
                section.getRow(builder, row - coord.getY(), col - coord.getX(),endCol - col);
                col += Math.min(endCol - col, section.getColumns()) - (col - coord.getX());
            }
        }
        while(col < endCol)
        {
            builder.append(FILLER);
            col++;
        }
    }
    
    /**
     * {@inheritDoc}
     * In this case call the update method of every contained section.
     */
    @Override
    public void update()
    {
        for(var section : sections.values())
            section.update();
        
        super.update();
    }
}
