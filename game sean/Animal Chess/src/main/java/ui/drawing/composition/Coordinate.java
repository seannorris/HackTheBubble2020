package ui.drawing.composition;

/**
 * Represents a coordinate of a section in a SectionComposer.
 */
public class Coordinate implements Comparable<Coordinate>
{
    private final int x;
    private final int y;
    
    /**
     * Constructor for Coordinate.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Shortcut method to get Coordinate object for specified position.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The Coordinate object.
     */
    public static Coordinate at(int x, int y)
    {
        return new Coordinate(x, y);
    }
    
    /**
     * Getter for the x coordinate.
     * @return The x coordinate.
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * Getter for the y coordinate.
     * @return The y coordinate.
     */
    public int getY()
    {
        return y;
    }
    
    /**
     * Returns true if two coordinates are equal.
     * @param in The coordinate to check.
     * @return Boolean; true if the two coordinates are equal.
     */
    public boolean equals(Object in)
    {
        if(!(in instanceof Coordinate))
            return super.equals(in);
        var coord = (Coordinate)in;
        return coord.getX() == x && coord.getY() == y;
    }
    
    /**
     * Used by hash sets and maps to improve efficiency.
     * Should be unique as possible which is done by adding one component of the coordinate to a large multiple of the other.
     * @return An as unique as possible integer value representing the piece.
     */
    public int hashcode()
    {
        return x + y * 1000;
    }
    
    /**
     * Used by the SectionComposer to order the sections in x order primarily.
     * @param o The coordinate to compare to.
     * @return An integer that's sign determines the sorting of the two coordinates.
     */
    @Override
    public int compareTo(Coordinate o)
    {
        if(o.x == x)
            return y - o.y;
        
        return x - o.x;
    }
}
